package se.redseven.edi.utils;

import static se.redseven.edi.utils.UNARecordParser.parseEdiString;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.edi.EDIFACTSettings;
import se.redseven.edi.EdiWriter;
import se.redseven.edi.components.Record;
import se.redseven.edi.components.UNA;
import se.redseven.edi.components.UNB;
import se.redseven.edi.components.UNH;
import se.redseven.edi.components.UNT;
import se.redseven.edi.components.UNZ;

public class CompareMessage {

    private static final Logger LOG = LoggerFactory.getLogger(CompareMessage.class);

    private static final String staticTestMessage =
        "UNA:+.? 'UNB+IATB:1+6XPPC+LHPPC+940101:0950+1'UNH+1+PAORES:93:1:IA'MSG+1:45'"
            + "IFT+3+XYZCOMPANY AVAILABILITY'ERC+A7V:1:AMD'IFT+3+NO MORE FLIGHTS'ODI'"
            + "TVL+240493:1000::1220+FRA+JFK+DL+400+C'PDI++C:3+Y::3+F::1'APD+74C:0:::6++++++6X'"
            + "TVL+240493:1740::2030+JFK+MIA+DL+081+C'PDI++C:4'APD+EM2:0:1630::6+++++++DA'UNT+13+1'UNZ+1+1'";

    private String referenceMessage = null;
    private String evaluateMessage = null;
    private List<String> messageLog = new ArrayList<String>();

    /**
     *
     * @param referenceMessage - EDI-string that is the reference to compare against
     * @param evaluateMessage - EDI-string that is compared against the 'referenceMessage'
     */
    public CompareMessage(String referenceMessage, String evaluateMessage) {

        this.referenceMessage = referenceMessage;
        this.evaluateMessage = evaluateMessage;
    }

    public static boolean diagnosticCheck() {

        CompareMessage cm = new CompareMessage(getStaticTestMessage(), getGeneratedTestMessage());

        int compareMessage;

        try {
            compareMessage = cm.compareMessage();

            for (String diffEntry : cm.getMessageLog()) {

                LOG.info(diffEntry);
            }

            return 0 == compareMessage;
        }
        catch (Exception ex) {

            LOG.info("diagnosticCheck execption", ex);
        }

        return false;
    }

    /**
     * Gets the list of differences
     * @return List with differences or zero sized list
     */
    public List<String> getMessageLog() {

        return messageLog;
    }

    /**
     * A test message that should be equal to the generated test message (@see staticTestMessage)
     * @return message
     */
    public static String getStaticTestMessage() {

        return staticTestMessage;
    }

    /**
     * Generates a test message that should compare equals to the static test message (@see getGeneratedTestMessage)
     * @return message
     */
    public static String getGeneratedTestMessage() {

        final EDIFACTSettings ediSettings = new EDIFACTSettings();
        ediSettings.setEdiDecimalNotation('.');

        final EdiWriter ediW = new EdiWriter(ediSettings);

        ediW.record(new UNA(ediSettings));

        //
        // -- Create UNB --
        //
        UNB unb = (UNB) ediW.record(new UNB());

        // Create composite S001
        UNB.S001 s001 = unb.new S001();
        s001.SyntaxIdentifier_0001 = "IATB";
        s001.SyntaxVersionNumber_0002 = "1";
        unb.S001.add(s001);

        // Create composite S002
        UNB.S002 s002 = unb.new S002();
        s002.InterchangeSenderIdentification_0004 = "6XPPC";
        unb.S002.add(s002);

        // Create composite S003
        UNB.S003 s003 = unb.new S003();
        s003.InterchangeRecipientIdentification_0010 = "LHPPC";
        unb.S003.add(s003);

        // Create composite S004
        UNB.S004 s004 = unb.new S004();
        s004.Date_0017 = "940101";
        s004.Time_0019 = "0950";
        unb.S004.add(s004);

        unb.InterchangeControlReference_0020 = "1";

        //
        // -- Create UNH --
        //
        UNH unh = (UNH) ediW.record(new UNH());
        unh.MessageReferenceNumber_0062 = "1";

        // Create composite S004
        UNH.S009 s009 = unh.new S009();
        s009.MessageType_0065 = "PAORES";
        s009.MessageVersionNumber_0052 = "93";
        s009.MessageReleaseNumber_0054 = "1";
        s009.ControllingAgencyCoded_0051 = "IA";
        unh.S009.add(s009);

        ediW.record("MSG").composite("1", "45");
        ediW.record("IFT").element("3").element("XYZCOMPANY AVAILABILITY");
        ediW.record("ERC").composite("A7V", "1", "AMD");
        ediW.record("IFT").element("3").element("NO MORE FLIGHTS");
        ediW.record("ODI");

        Record tvl = ediW.record("TVL").composite("240493", "1000", "", "1220");
        tvl.element("FRA").element("JFK").element("DL").element("400").element("C");

        Record pdi = ediW.record("PDI").element("").composite("C", "3").composite("Y", "", "3");
        pdi.composite("F", "", "1");
        ediW.record("APD").composite("74C", "0", "", "", "6").elementRep(5).element("6X");

        Record tvl2 = ediW.record("TVL").composite("240493", "1740", "", "2030").element("JFK");
        tvl2.element("MIA").element("DL").element("081").element("C");
        ediW.record("PDI").element("").composite("C", "4");
        ediW.record("APD").composite("EM2", "0", "1630", "", "6").elementRep(6).element("DA");

        //
        // -- Create UNT --
        //
        UNT unt = new UNT();
        unt.MessageReferenceNumber_0062 = unh.MessageReferenceNumber_0062;
        ediW.record(unt);

        //
        // -- Create UNZ --
        //
        UNZ unz = new UNZ();
        unz.InterchangeControlReference_0020 = unb.InterchangeControlReference_0020;
        ediW.record(unz);

        String ediMsg = ediW.getEdiMessage();
        LOG.debug(String.format("edi message: [%s]", ediMsg));
        return ediMsg;
    }

    public static String[] rowSplit(String message, char ediSegmentSeparator) {

        final String segmentSplitRegexp = String.format("(?<!\\?)%c", ediSegmentSeparator);

        String[] splitedMessage = message.split(segmentSplitRegexp);

        for (int i = 0; i < splitedMessage.length; i++) {

            splitedMessage[i] = splitedMessage[i] + "'";
        }

        return splitedMessage;
    }

    /**
     *
     * @return
     * -1 = if referenceMessage has more lines.
     * 0  = if booth messages have the same line count and are equal.
     * 1  = if evaluateMessageLines has more lines.
     * @throws EdiParserException
     */
    public int compareMessage() throws EdiParserException {

        messageLog = new ArrayList<String>();

        char refSep = parseEdiString(referenceMessage).getEdiSegmentSeparator();
        String[] referenceMessageLines = rowSplit(referenceMessage, refSep);
        int referenceMessageLinesCount = null != referenceMessageLines ? referenceMessageLines.length : 0;

        char evlSep = parseEdiString(evaluateMessage).getEdiSegmentSeparator();
        String[] evaluateMessageLines = rowSplit(evaluateMessage, evlSep);
        int evaluateMessageLinesCount = null != evaluateMessageLines ? evaluateMessageLines.length : 0;

        int row = 0;

        LOG.debug(String.format("referenceMessageLines count: %d", referenceMessageLinesCount));
        LOG.debug(String.format("evaluateMessageLines count: %d", evaluateMessageLinesCount));

        // Loop over largest message
        int loopCnt = Math.max(referenceMessageLines.length, evaluateMessageLines.length);

        int cmpResultRef = 0;
        int cmpResultEval = 0;

        for (int i = 0; i < loopCnt; i++) {

            row = i + 1;

            String refMsg = referenceMessageLines.length > i ? referenceMessageLines[i] : "";
            String evalMsg = evaluateMessageLines.length > i ? evaluateMessageLines[i] : "";

            String compareStr = "";
            int cmp = evalMsg.compareTo(refMsg);

            if (cmp != 0) {

                compareStr = String.format("%04d %s (expected: %s)", row, evalMsg, refMsg);
                cmpResultRef += cmp;
            }
            else {

                compareStr = String.format("%04d %s ", row, refMsg);
            }

            LOG.debug(compareStr);

            messageLog.add(compareStr);
        }

        int max = Math.max(cmpResultRef, cmpResultEval);

        return max > 0 ? max : Integer.compare(referenceMessageLinesCount, evaluateMessageLinesCount);
    }
}
