package se.redseven.edi;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.ediwriter.EDIFACTSettings;
import se.redseven.ediwriter.EdiWriter;
import se.redseven.ediwriter.Record;
import se.redseven.ediwriter.ReferenceGeneration;
import se.redseven.ediwriter.UNA;
import se.redseven.ediwriter.UNB;
import se.redseven.ediwriter.UNH;
import se.redseven.ediwriter.UNT;
import se.redseven.ediwriter.UNZ;
import se.redseven.ediwriter.utils.EdiUtils;
import se.redseven.ediwriter.utils.UNARecordParser;

/**
 * Testklass för classen {@link EdiWriter}.
 * @author ICC
 */
public class EdiWriterTest {

    private static final Logger LOG = LoggerFactory.getLogger(EdiWriterTest.class);

    /**
     * EdiWriter Diagnostisk test.
     */
    @Test
    public void diagnosticCheckTest() {

        LOG.info("Compare generated with static message check");

        final String staticTestMessage = "UNA:+.? 'UNB+IATB:1+6XPPC+LHPPC+940101:0950+1'UNH+1+PAORES:93:1:IA'MSG+1:45'"
            + "IFT+3+XYZCOMPANY AVAILABILITY'ERC+A7V:1:AMD'IFT+3+NO MORE FLIGHTS'ODI'"
            + "TVL+240493:1000::1220+FRA+JFK+DL+400+C'PDI++C:3+Y::3+F::1'APD+74C:0:::6++++++6X'"
            + "TVL+240493:1740::2030+JFK+MIA+DL+081+C'PDI++C:4'APD+EM2:0:1630::6+++++++DA'UNT+13+1'UNZ+1+1'";

        final EDIFACTSettings ediSettings = new EDIFACTSettings();
        ediSettings.setDecimalNotation('.');

        final EdiWriter ediW = new EdiWriter(ediSettings);

        ediW.record(new UNA(ediSettings));

        //
        // -- Create UNB --
        //
        UNB unb = ediW.record(new UNB());

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
        UNH unh = ediW.record(new UNH());
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
        ediW.record("ODI").setTruncate(false);

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

        assertTrue("true", compareMessage(staticTestMessage, ediMsg));
    }

    /**
     * Test av trunkering.
     */
    @Test
    public void testUNBGeneration() {

        final String testUnb = "UNB+UNOC:2+2321000123:X:S000011+KLAB:X:KLAB+151112:0922+BCUZ3++MEDREQ++2'";

        final EDIFACTSettings ediSettings = new EDIFACTSettings();
        ediSettings.setDecimalNotation('.');

        final EdiWriter ediW = new EdiWriter(ediSettings);

        UNB unb = ediW.record(new UNB());
        unb.setTruncate(false);

        // Create composite S001
        UNB.S001 s001 = unb.new S001();
        s001.SyntaxIdentifier_0001 = "UNOC";
        s001.SyntaxVersionNumber_0002 = "2";
        unb.S001.add(s001);

        // Create composite S002
        UNB.S002 s002 = unb.new S002();
        s002.InterchangeSenderIdentification_0004 = "2321000123";
        s002.IdentificationCodeQualifier_0007 = "X";
        s002.InterchangeSenderInternalIdentification_0008 = "S000011";
        unb.S002.add(s002);

        // Create composite S003
        UNB.S003 s003 = unb.new S003();
        s003.InterchangeRecipientIdentification_0010 = "KLAB";
        s003.IdentificationCodeQualifier_0007 = "X";
        s003.InterchangeRecipientInternalIdentification_0014 = "KLAB";

        unb.S003.add(s003);

        // Create composite S004
        UNB.S004 s004 = unb.new S004();
        s004.Date_0017 = "151112";
        s004.Time_0019 = "0922";
        unb.S004.add(s004);

        unb.InterchangeControlReference_0020 = "BCUZ3";
        unb.ApplicationReference_0026 = "MEDREQ";
        unb.AcknowledgementRequest_0031 = "2"; // 1 = Kvitto begärs, 2 = APERAK begärs

        /*
        060     S005    RECIPIENT'S REFERENCE/PASSWORD DETAILS  C   1
        0022    Recipient reference/password    M       an..14
        0025    Recipient reference/password qualifier  C       an2
        070     0026    Application reference   C   1   an..14
        080     0029    Processing priority code    C   1   a1
        090     0031    Acknowledgement request C   1   n1
        100     0032    Interchange agreement identifier    C   1   an..35
        110     0035    Test indicator
        */

        String ediMsg = ediW.getEdiMessage();
        LOG.debug(String.format("unb: [%s]", ediMsg));
        Assert.assertEquals("UNB generation not correct!", testUnb, ediMsg);
    }

    /**
     * Test av trunkering.
     */
    @Test
    public void testTruncate() {

        String dataRef = "UNB+UNOC:2+CCIS:X:MGK+NLKELA:X:MGK+141112:0708+23714++APERAK'";
        String dataTest = "UNB+UNOC:2+CCIS:X:MGK+NLKELA:X:MGK+141112:0708::::::::::+23714++APERAK++++++++'";
        String resultStr = EdiUtils.truncateString(dataTest, new EDIFACTSettings());

        LOG.info(String.format("Ref.:%s", dataRef));
        LOG.info(String.format("Tst.:%s", dataTest));
        LOG.info(String.format("Res.:%s", resultStr));

        Assert.assertEquals("Reference and generated message are not the same", dataRef, resultStr);

        dataRef = "";
        dataTest = "TEM+:::+++++:::'";
        resultStr = EdiUtils.truncateString(dataTest, new EDIFACTSettings());

        LOG.info(String.format("Ref.:%s", dataRef));
        LOG.info(String.format("Tst.:%s", dataTest));
        LOG.info(String.format("Res.:%s", resultStr));

        Assert.assertEquals("Reference and generated message are not the same", dataRef, resultStr);
    }

    /**
     * Test av UNA parsning.
     */
    @Test
    public void testParseUNA() {

        String una = "UNA:+.? '";

        final EDIFACTSettings ediSettings = new EDIFACTSettings();
        ediSettings.setSegmentUNAFromString(una);

        Assert.assertEquals("CompositeSeparator", ediSettings.getCompositeSeparator(), ':');
        Assert.assertEquals("ElementSeparator", ediSettings.getElementSeparator(), '+');
        Assert.assertEquals("DecimalNotation", ediSettings.getDecimalNotation(), '.');
        Assert.assertEquals("ReleaseCharcter", ediSettings.getReleaseCharcter(), '?');
        Assert.assertEquals("PadCharcter", ediSettings.getPadCharcter(), ' ');
        Assert.assertEquals("RecordSeparator", ediSettings.getRecordSeparator(), '\'');
    }

    /**
     * Test av UNA parsning.
     */
    @Test(expected = java.lang.RuntimeException.class)
    public void negTestParseUNA() {

        String una = "aaa";

        final EDIFACTSettings ediSettings = new EDIFACTSettings();
        ediSettings.setSegmentUNAFromString(una);

        Assert.assertEquals("CompositeSeparator", ediSettings.getCompositeSeparator(), ':');
        Assert.assertEquals("ElementSeparator", ediSettings.getElementSeparator(), '+');
        Assert.assertEquals("DecimalNotation", ediSettings.getDecimalNotation(), '.');
        Assert.assertEquals("ReleaseCharcter", ediSettings.getReleaseCharcter(), '?');
        Assert.assertEquals("PadCharcter", ediSettings.getPadCharcter(), ' ');
        Assert.assertEquals("RecordSeparator", ediSettings.getRecordSeparator(), '\'');
    }

    /**
     * Test write MedRPT.
     */
    @Test
    public void testWriteMedRPT() {

        final EDIFACTSettings ediSettings = new EDIFACTSettings();
        ediSettings.setDecimalNotation('.');

        final EdiWriter ediW = new EdiWriter(ediSettings);

        ediW.record(new UNA(ediSettings));

        //
        // -- Create UNB --
        //
        UNB unb = ediW.record(new UNB());
        UNB.S001 s001 = unb.createS001();
        s001.SyntaxIdentifier_0001 = "UNOC";
        s001.SyntaxVersionNumber_0002 = "2";

        UNB.S002 s002 = unb.createS002();
        s002.InterchangeSenderIdentification_0004 = "CCIS";
        s002.IdentificationCodeQualifier_0007 = "X";
        s002.InterchangeSenderInternalIdentification_0008 = "MGK";

        UNB.S003 s003 = unb.createS003();
        s003.InterchangeRecipientIdentification_0010 = "NLKELA";
        s003.IdentificationCodeQualifier_0007 = "X";
        s003.InterchangeRecipientInternalIdentification_0014 = "MGK";

        UNB.S004 s004 = unb.createS004();
        s004.Date_0017 = "141112";
        s004.Time_0019 = "0708";

        unb.InterchangeControlReference_0020 = ReferenceGeneration.getReference(5);
        unb.ApplicationReference_0026 = "APERAK";

        UNH unh = ediW.record(new UNH());
        unh.MessageReferenceNumber_0062 = ReferenceGeneration.getReference(5);

        UNH.S009 s009 = unh.new S009();
        s009.MessageType_0065 = "APERAK";
        s009.AssociationAssignedCode_0057 = "BLL001";
        s009.MessageVersionNumber_0052 = "95B";
        s009.ControllingAgencyCoded_0051 = "UN";
        s009.MessageReleaseNumber_0054 = "D";

        unh.S009.add(s009);

        ediW.record("BMG").element("997");
        ediW.record("RFF").composite("Z01", "R12064331");

        UNT unt = ediW.record(new UNT());
        unt.NumberOfSegmentsInMessage_0074 = "4";
        unt.MessageReferenceNumber_0062 = unh.MessageReferenceNumber_0062;

        UNZ unz = ediW.record(new UNZ());
        unz.InterchangeControlCount_0036 = "1";
        unz.InterchangeControlReference_0020 = unb.InterchangeControlReference_0020;

        String ediString = ediW.getEdiMessage();

        final String expectedEdifact = String.format(
            "UNA:+.? 'UNB+UNOC:2+CCIS:X:MGK+NLKELA:X:MGK+141112:0708+%s+APERAK'UNH+%s+APERAK:95B:D:UN:BLL001'"
                + "BMG+997'RFF+Z01:R12064331'UNT+4+%s'UNZ+1+%s'",
            unb.InterchangeControlReference_0020, unh.MessageReferenceNumber_0062, unt.MessageReferenceNumber_0062,
            unz.InterchangeControlReference_0020);

        //LOG.debug("Transformed EDI {}", ediString);

        compareMessage(expectedEdifact, ediString);
    }

    /**
     * Jämför EDIFACT-meddelande med varandra (diff).
     * @param referenceMessage
     * @param evaluateMessage
     * @return
     */
    private boolean compareMessage(String referenceMessage, String evaluateMessage) {

        char refSep = UNARecordParser.parseEdiString(referenceMessage).getRecordSeparator();
        String[] referenceMessageLines = EdiUtils.rowSplit(referenceMessage, refSep);
        int referenceMessageLinesCount = null != referenceMessageLines ? referenceMessageLines.length : 0;

        char evlSep = UNARecordParser.parseEdiString(evaluateMessage).getRecordSeparator();
        String[] evaluateMessageLines = EdiUtils.rowSplit(evaluateMessage, evlSep);
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
            } else {

                compareStr = String.format("%04d %s ", row, refMsg);
            }

            LOG.info(compareStr);
        }

        if (cmpResultRef < cmpResultEval) {

            LOG.debug(String.format("cmpResultRef: %d cmpResultEval: %d", cmpResultRef, cmpResultEval));

            return false;
        } else if (cmpResultRef > cmpResultEval) {

            LOG.debug(String.format("cmpResultRef: %d cmpResultEval: %d", cmpResultRef, cmpResultEval));

            return false;
        }

        return true;
    }
}
