package se.redseven.ediwriter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.ediwriter.meta.annotation.EdiComposite;
import se.redseven.ediwriter.meta.annotation.EdiElement;
import se.redseven.ediwriter.meta.annotation.EdiRecord;
import se.redseven.ediwriter.utils.EdiUtils;

/**
 * A <tt>EdiWriter</tt> writes and escapes EDIFACT.
 * EdiWriter is the main class for generation of EDIFACT.
 */
public class EdiWriter extends AnnotationProcessor implements Constants {

    private static final Logger LOG = LoggerFactory.getLogger(EdiWriter.class);

    private EDIFACTSettings ediSettings = null;
    private ArrayList<Record> recordList = new ArrayList<Record>();

    /**
     * Constructor for EdiWriter with default EDIFACTSettings.
     */
    public EdiWriter() {

        this(new EDIFACTSettings());
    }

    /**
     * Constructor for EdiWriter, the main class for generation of EDIFACT.
     * @param ediSettings EDIFACTSettings with your settings.
     */
    public EdiWriter(EDIFACTSettings ediSettings) {

        if (ediSettings != null) {

            this.ediSettings = ediSettings;
        } else {

            this.ediSettings = new EDIFACTSettings();
        }

        LOG.debug(String.format("EDIFACTSettings: %s", ediSettings));
    }

    /**
     * EDIFACT-Settings.
     * @return the ediSettings.
     */
    public EDIFACTSettings getEdiSettings() {

        return ediSettings;
    }

    /**
     * Creates a new Record and adds the Record to the collection.
     * @param recordName name of the Record.
     * @return Return a Record instance.
     */
    public Record record(String recordName) {

        Record record = new Record();
        record.setName(recordName);
        recordList.add(record);

        return record;
    }

    /**
     * Adds the Record to the collection.
     * @param <T> Type parameter.
     * @param record add a created Record.
     * @return the same Record as the input.
     */
    public <T extends Record> T record(T record) {

        recordList.add(record);
        return record;
    }

    /**
     * Convenient method for UNA Record.
     * @return UNA Record.
     */
    public UNA createUNA() {

        return record(new UNA(ediSettings));
    }

    /**
     * Get a String representation of the EDIFACT message.
     * @return The formated EDIFACT Message.
     */
    public String getEdiMessage() {

        StringBuffer msg = new StringBuffer();

        int messageInInterchange = 0;
        int recordsInMessage = 0;

        LOG.debug(String.format("Record count: %d", recordList.size()));

        for (Record record : recordList) {

            String recordContent = getRecord(record);

            LOG.debug(String.format("Record: '%s'", recordContent));

            if (recordContent == null || recordContent.equals("")) {

                continue;
            }

            recordsInMessage++;

            if (record.getName().equals(UNA_RECORD)) {

                messageInInterchange = 0;
                recordsInMessage = 0;
            } else if (record.getName().equals(UNB_INTERCHANGEHEADER)) {

                messageInInterchange++;
            } else if (record.getName().equals(UNH_MESSAGE_HEADER)) {

                recordsInMessage = 1;
            } else if (record.getName().equals(UNT_MESSAGE_TRAILER)) {

                recordContent = recordContent.replace(Constants.RECORD_COUNT, String.format("%d", recordsInMessage));
            } else if (record.getName().equals(UNZ_INTERCHANGE_TRAILER)) {

                recordContent =
                    recordContent.replace(Constants.INTERCHANGE_COUNT, String.format("%d", messageInInterchange));
            }

            msg.append(recordContent);
        }

        return msg.toString();
    }

    /**
     * Get the formated record using EDIFACTSettings.
     * @param inRecord Record to format/get
     * @return String with a formated record
     */
    public String getRecord(Record inRecord) {

        Record record = inRecord;
        final StringBuffer recordValueBuffer = new StringBuffer();
        String recordString = null;
        Class<? extends Record> clazz = record.getClass();

        if (inRecord.getName().equals(UNA.class.getSimpleName())) {

            return ((UNA) inRecord).getUNARecord();
        }

        if (clazz.isAnnotationPresent(EdiRecord.class)) {

            // Get annotations to record

            Field[] fields = clazz.getFields();
            record = processClassFields(record, fields);
        }

        // Read Elements and Composites
        for (AbstractData abstractData : record.getRecordList()) {

            if (abstractData instanceof Element) {

                recordValueBuffer.append(((Element) abstractData).getFormatedValue(ediSettings));
            } else if (abstractData instanceof Composite) {

                recordValueBuffer.append(((Composite) abstractData).getFormatedValue(ediSettings));
            }
        }

        recordString =
            String.format("%s%s%c", record.getName(), recordValueBuffer.toString(), ediSettings.getRecordSeparator());

        if (ediSettings.isTruncating() && record.isTruncating()) {

            recordString = EdiUtils.truncateString(recordString, ediSettings);
        }

        return recordString;
    }

    /**
     * Get a Record with all data associated with its annotated object.
     * @param record
     * @param fields
     * @return Record with updated list
     */
    private Record processClassFields(Record record, Field[] fields) {

        String recName = record.getClass().getSimpleName();
        ArrayList<AbstractData> localRecordList = record.getRecordList();
        Record outRecord = new Record(recName);

        for (Field field : fields) {

            //LOG.debug(String.format("Empty composite: '%s' record '%s'.", field.getName(), recName));

            if (field.isAnnotationPresent(EdiComposite.class)) {

                // compositeGroup can be n:th composite(s)!
                List<Composite> compositeGroup = getCompositeList(field, record);

                if (checkCompositeRepetitions(field, compositeGroup)) {

                    if (0 == compositeGroup.size()) {

                        // Add empty element
                        //LOG.debug(String.format("Empty composite: '%s' record '%s'.", field.getName(), recName));
                        localRecordList.add(new Element(""));
                    } else {
                        // Loop over compositeGroup
                        for (Composite composite : compositeGroup) {

                            Class<? extends Composite> compositeClass = composite.getClass();
                            String compositeName = compositeClass.getSimpleName();
                            Field[] compositeFields = compositeClass.getFields();

                            LOG.debug(String.format("Composite: '%s' record '%s'.", compositeName, recName));

                            //composite.setValues()
                            ArrayList<String> elementValueList = new ArrayList<String>();
                            for (Field compositeField : compositeFields) {

                                if (compositeField.isAnnotationPresent(EdiElement.class)) {

                                    // Get Value
                                    String elementValue = getElementValue(compositeField, composite);

                                    if (null != elementValue) {

                                        elementValueList.add(elementValue);
                                    }
                                }
                            }

                            if (elementValueList.size() > 0) {

                                outRecord.composite(elementValueList);
                            }
                        }
                    }
                }
            } else if (field.isAnnotationPresent(EdiElement.class)) {

                // Get Value
                String elementValue = getElementValue(field, record);

                if (null != elementValue) {

                    outRecord.element(elementValue);
                }
            }
        }

        return outRecord;
    }

    @Override
    public String toString() {

        return getEdiMessage();
    }
}
