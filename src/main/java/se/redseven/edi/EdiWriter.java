package se.redseven.edi;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.edi.components.Record;
import se.redseven.edi.components.UNA;

/**
 * A <tt>EdiWriter</tt> writes and escapes EDIFACT.
 * EdiWriter is the main class for generation of EDIFACT.
 */
public class EdiWriter implements Constants {

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
        }
        else {

            this.ediSettings = new EDIFACTSettings();
        }

        LOG.debug(String.format("EDIFACTSettings: %s", ediSettings));
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
     * @param <T> Type parameter, probably Element or Composite
     * @param record add a created Record.
     * @return the same Record as the input.
     */
    public <T extends Record> T record(T record) {

        recordList.add(record);
        return record;
    }

    /**
     * Convenient method for UNA Record
     * @return UNA Record.
     */
    public UNA createUNA() {

        return record(new UNA(ediSettings));
    }

    /**
     * Get a String representation of the EDIFACT message
     * @return The formated EDIFACT Message.
     */
    public String getEdiMessage() {

        StringBuffer msg = new StringBuffer();

        int messageInInterchange = 0;
        int recordsInMessage = 0;

        for (Record record : recordList) {

            String recordContent = record.getRecord(ediSettings);
            recordsInMessage++;

            if (record.getName().equals(UNA_Record)) {

                messageInInterchange = 0;
                recordsInMessage = 0;
            }
            else if (record.getName().equals(UNB_Interchange_Header)) {

                messageInInterchange++;
            }
            else if (record.getName().equals(UNH_Message_Header)) {

                recordsInMessage = 1;
            }
            else if (record.getName().equals(UNT_Message_Trailer)) {

                recordContent = recordContent.replace(Constants.RECORD_COUNT, String.format("%d", recordsInMessage));
            }
            else if (record.getName().equals(UNZ_Interchange_Trailer)) {

                recordContent =
                    recordContent.replace(Constants.INTERCHANGE_COUNT, String.format("%d", messageInInterchange));
            }

            msg.append(recordContent);
        }

        return msg.toString();
    }

    @Override
    public String toString() {

        return getEdiMessage();
    }
}
