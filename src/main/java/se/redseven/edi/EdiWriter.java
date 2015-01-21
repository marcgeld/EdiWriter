package se.redseven.edi;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.edi.components.Record;
import se.redseven.edi.components.UNA;
import se.redseven.edi.utils.CompareMessage;
import se.redseven.edi.utils.EdiParserException;

/**
 * A <tt>EdiWriter</tt> writes and escapes EDIFACT
 *
 * @param <EDIFACTSettings>
 */
public class EdiWriter implements Constants {

    private static final Logger LOG = LoggerFactory.getLogger(EdiWriter.class);

    private EDIFACTSettings ediSettings = null;
    private ArrayList<Record> recordList = new ArrayList<Record>();

    public EdiWriter(EDIFACTSettings ediSettings) {

        if (ediSettings != null) {

            this.ediSettings = ediSettings;
        } else {

            this.ediSettings = new EDIFACTSettings();
        }
    }

    public Record record(String recordName) {

        Record record = new Record();
        record.setName(recordName);
        recordList.add(record);

        return record;
    }

    public <T extends Record> T record(T record) {
        recordList.add(record);
        return record;
    }

    public UNA createUNA() {

        return record(new UNA(ediSettings));
    }

    /*
    private String endSegmentAndTruncate(String segment) {

        segment += ediSettings.ediSegmentSeparator;

        if (ediSettings.truncate) {

            // TODO: Add regexp to truncate xml...
        }
        if (ediSettings.linebreaks) {

            segment += ediSettings.lineSeparator;
        }

        return segment;
    }
     */

    public String getEdiMessage() {

        StringBuffer msg = new StringBuffer();

        int messageInInterchange = 0;
        int recordsInMessage = 0;

        for (Record record : recordList) {

            String recordContent = record.getRecord(ediSettings);
            recordsInMessage++;

            if (record.getName().equals(UNA_Segment)) {

                messageInInterchange = 0;
                recordsInMessage = 0;
            } else if (record.getName().equals(UNB_Interchange_Header)) {

                messageInInterchange++;
            } else if (record.getName().equals(UNH_Message_Header)) {

                recordsInMessage = 1;
            } else if (record.getName().equals(UNT_Message_Trailer)) {

                recordContent = recordContent.replace(Constants.SEGMENTS_COUNT, String.format("%d", recordsInMessage));
            } else if (record.getName().equals(UNZ_Interchange_Trailer)) {

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

    public static void main(String[] args) throws EdiParserException {

        CompareMessage edic =
            new CompareMessage(CompareMessage.getStaticTestMessage(), CompareMessage.getGeneratedTestMessage());

        edic.compareMessage();

    }
}
