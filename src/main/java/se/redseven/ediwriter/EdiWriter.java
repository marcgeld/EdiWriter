package se.redseven.ediwriter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.ediwriter.error.ParserException;
import se.redseven.ediwriter.meta.annotation.EdiComposite;
import se.redseven.ediwriter.meta.annotation.EdiElement;
import se.redseven.ediwriter.meta.annotation.EdiRecord;

/**
 * A <tt>EdiWriter</tt> writes and escapes EDIFACT.
 * EdiWriter is the main class for generation of EDIFACT.
 */
public class EdiWriter implements Constants {

    private static final Logger LOG = LoggerFactory.getLogger(EdiWriter.class);

    private static final String REPETITIONS_ERROR_MAX =
        "Repetitions error: max rep. is %d, actually rep. is %d for field '%s' class '%s'.";
    private static final String REPETITIONS_ERROR_MIN =
        "Repetitions error: min rep. is %d, actually rep. is %d for field '%s' class '%s'.";
    private static final String ERROR_ACCESSING_OBJECT = "Error: Accessing object on: '%s' class '%s'.";
    private static final String ERROR_ELEMENT_HAS_NO_VALUE =
        "Error: Element '%s' has no value '%s' (lenght=%d) but is mandatory! class '%s'.";
    private static final String ERROR_ELEMENT_TO_LONG =
        "Error: Element '%s' to long, value='%s' (lenght=%d), max=%d class '%s'.";
    private static final String ERROR_DEFINITION_JAVA_UTIL_LIST_IS_ALLOWED =
        "Error: Definition of annotated EdiSegment. Field '%s' class '%s'. Only types of <java.util.List> is allowed!";

    private EDIFACTSettings ediSettings = null;
    private ArrayList<Segment> segmentList = new ArrayList<Segment>();

    private int _interchangeCount = -1;
    private int _segmentCount = -1;
    //private String _interchangeControlReference = null;
    //private String _messageReference = null;

    /**
     * Constructor for EdiWriter with default EDIFACTSettings.
     */
    public EdiWriter() {

        this(new EDIFACTSettings());
    }

    void addRecord(Segment segment) {

        if (segment.getName().equals(UNA_RECORD)) {
            _interchangeCount = 0;
            _segmentCount = 0;
        } else if (segment.getName().equals(UNB_INTERCHANGEHEADER)) {
            _interchangeCount++;
        } else if (segment.getName().equals(UNH_MESSAGE_HEADER)) {
            _segmentCount = 1;
        } else if (segment.getName().equals(UNZ_INTERCHANGE_TRAILER)) {
            // NOP
        } else {
            _segmentCount++;
        }
        segmentList.add(segment);
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
    public Segment record(String recordName) {

        Segment record = new Segment();
        record.setName(recordName);
        addRecord(record);

        return record;
    }

    /**
     * Adds the Record to the collection.
     * @param <T> Type parameter.
     * @param record add a created Record.
     * @return the same Record as the input.
     */
    public <T extends Segment> T record(T record) {

        addRecord(record);
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

        LOG.debug(String.format("Record count: %d", segmentList.size()));

        for (Segment record : segmentList) {

            String recordContent = getSegment(record);

            LOG.debug(String.format("Record: '%s'", recordContent));

            if (recordContent == null || recordContent.equals("")) {

                continue;
            }

            msg.append(recordContent);
        }

        return msg.toString();
    }

    /**
     * Get the formated record using EDIFACTSettings.
     * @param inSegment Record to format/get
     * @return String with a formated record
     */
    public String getSegment(Segment inSegment) {

        Segment segment = inSegment;
        final StringBuffer recordValueBuffer = new StringBuffer();
        String recordString = null;
        Class<? extends Segment> clazz = segment.getClass();

        if (inSegment.getName().equals(UNA.class.getSimpleName())) {

            return ((UNA) inSegment).getUNARecord();
        }

        if (clazz.isAnnotationPresent(EdiRecord.class)) {

            // Get annotations to record
            Field[] fields = clazz.getFields();
            segment = processClassFields(segment, fields);
        }

        // Read Elements and Composites
        for (AbstractData abstractData : segment.getRecordList()) {

            if (abstractData instanceof Element) {

                recordValueBuffer.append(((Element) abstractData).getFormatedValue(ediSettings));
            } else if (abstractData instanceof Composite) {

                recordValueBuffer.append(((Composite) abstractData).getFormatedValue(ediSettings));
            }
        }

        recordString =
            String.format("%s%s%c", segment.getName(), recordValueBuffer.toString(), ediSettings.getRecordSeparator());

        if (ediSettings.isTruncating() && segment.isTruncating()) {

            recordString = EdiUtils.truncateString(recordString, ediSettings);
        }

        return recordString;
    }

    /**
     * Get values from an element.
     *
     * @param field the active field.
     * @param obj object with the element (field).
     * @return Value of that element of that object.
     */
    public String getElementValue(Field field, Object obj) {

        String cName = obj.getClass().getSimpleName();
        String fName = field.getName();

        EdiElement ediElement = field.getAnnotation(EdiElement.class);
        boolean elementMandatory = ediElement.mandatory();
        int elementLength = ediElement.length();
        String defValue = ediElement.value();

        Object elemObj = getObject(field, obj);
        String value = String.valueOf(elemObj);

        if (StringUtils.equals(defValue, INTERCHANGE_COUNT)) {
            value = String.format("%d", _interchangeCount);
        } else if (StringUtils.equals(defValue, RECORD_COUNT)) {
            value = String.format("%d", _segmentCount);
        }

        if ("null".equals(value)) {
            value = StringUtils.defaultString(defValue);
        }

        LOG.debug(String.format("Element '%s' value '%s' default value '%s' max-lenght=%d class '%s'.", fName, value,
            defValue, elementLength, cName));

        // Check if Mandatory
        if (elementMandatory && StringUtils.isBlank(value))

        {

            throw new ParserException(String.format(ERROR_ELEMENT_HAS_NO_VALUE, fName, value, elementLength, cName));
        }

        if (StringUtils.defaultString(value).length() > elementLength)

        {

            throw new ParserException(String.format(ERROR_ELEMENT_TO_LONG, fName, value, elementLength,
                StringUtils.defaultString(value).length(), cName));
        }

        return value.length() > 0 ? value : null;

    }

    /**
     * Get all repetitions of a composite. A entry can be defined a composite
     * OR it can be a repeating group of composites.
     *
     * @param field the Composite OR group of composites.
     * @param listObj the object that has the composite.
     * @return A List with n:th Composite(s)
     */
    public List<Composite> getCompositeList(Field field, Object listObj) {

        String cName = listObj.getClass().getSimpleName();
        String fName = field.getName();
        EdiComposite ediSegment = field.getAnnotation(EdiComposite.class);
        int repMax = ediSegment.repMax();
        int repMin = ediSegment.repMin();

        listObj = getObject(field, listObj);

        if (!(listObj instanceof List)) {

            throw new ParserException(String.format(ERROR_DEFINITION_JAVA_UTIL_LIST_IS_ALLOWED, fName, cName));
        }

        LOG.debug(String.format("Composite '%s' (%d/%d) class '%s'", fName, repMin, repMax, cName));

        @SuppressWarnings("unchecked")
        List<Composite> compositeList = (List<Composite>) listObj;

        return compositeList;
    }

    /**
     * Get field object.
     *
     * @param field the Active field.
     * @param listObj the object that has the field.
     * @return Object
     */
    private Object getObject(Field field, Object listObj) {

        String cName = listObj.getClass().getSimpleName();
        String fName = field.getName();
        Object obj = null;

        try {
            obj = field.get(listObj);
        } catch (IllegalArgumentException ex) {

            String errMsg = String.format(ERROR_ACCESSING_OBJECT, fName, cName);
            LOG.error(errMsg, ex);
            throw new ParserException(errMsg, ex);

        } catch (IllegalAccessException ex) {

            String errMsg = String.format(ERROR_ACCESSING_OBJECT, fName, cName);
            LOG.error(errMsg, ex);
            throw new ParserException(errMsg, ex);
        }

        return obj;
    }

    /**
     * Check repetitions against definitions.
     *
     * @param field the Active field to validate against the annotations.
     * @param compositeList list of composites available.
     * @return true if ok.
     * @throws ParserException if repetitions differs against definitions
     */
    public boolean checkCompositeRepetitions(Field field, List<Composite> compositeList) {

        EdiComposite ediSegment = field.getAnnotation(EdiComposite.class);
        String cName = compositeList.getClass().getSimpleName();
        String fName = field.getName();
        int repMax = ediSegment.repMax();
        int repMin = ediSegment.repMin();

        // Check repetitions
        if (compositeList.size() < repMin) {

            throw new ParserException(String.format(REPETITIONS_ERROR_MIN, repMin, compositeList.size(), fName, cName));

        } else if (compositeList.size() > repMax) {

            throw new ParserException(String.format(REPETITIONS_ERROR_MAX, repMax, compositeList.size(), fName, cName));
        }

        return true;
    }

    /**
     * Get a Record with all data associated with its annotated object.
     * @param record
     * @param fields
     * @return Record with updated list
     */
    private Segment processClassFields(Segment record, Field[] fields) {

        String segmentName = record.getClass().getSimpleName();
        Segment outSegment = new Segment(segmentName);

        for (Field field : fields) {

            if (field.isAnnotationPresent(EdiComposite.class)) {

                // compositeGroup can be n:th composite(s)!
                List<Composite> compositeGroup = getCompositeList(field, record);

                if (checkCompositeRepetitions(field, compositeGroup)) {

                    if (0 == compositeGroup.size()) {

                        // Add empty element grp.
                        outSegment.element("");
                    } else {
                        // Loop over compositeGroup
                        for (Composite composite : compositeGroup) {

                            Class<? extends Composite> compositeClass = composite.getClass();
                            String compositeName = compositeClass.getSimpleName();
                            Field[] compositeFields = compositeClass.getFields();

                            LOG.debug(String.format("Composite: '%s' record '%s'.", compositeName, segmentName));

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

                                outSegment.composite(elementValueList);
                            }
                        }
                    }
                }
            } else if (field.isAnnotationPresent(EdiElement.class)) {

                // Get Value
                String elementValue = getElementValue(field, record);

                if (null != elementValue) {

                    outSegment.element(elementValue);
                } else {
                    outSegment.element("");
                }
            }
        }

        return outSegment;
    }

    @Override
    public String toString() {

        return getEdiMessage();
    }
}
