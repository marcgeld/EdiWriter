package se.redseven.edi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.ediwriter.EDIFACTSettings;
import se.redseven.ediwriter.EdiWriter;
import se.redseven.ediwriter.UNA;
import se.redseven.ediwriter.UNB;
import se.redseven.ediwriter.UNH;
import se.redseven.ediwriter.UNT;
import se.redseven.ediwriter.UNZ;
import se.redseven.ediwriter.utils.CompareMessage;
import se.redseven.ediwriter.utils.EdiUtils;

/**
 * Testklass för classen {@link EdiWriter}.
 * @author ICC
 */
public class EdiWriterTest {

    private static final Logger LOG = LoggerFactory.getLogger(EdiWriterTest.class);

    /**
     * Test av output
     */
    @Test
    public void testEdiWriterTestOutputAsciiArt() {

        final String[] asciiArt =
            {
                "___________    .___.__ __      __        .__  __              ___________              __   ",
                "\\_   _____/  __| _/|__/  \\    /  \\_______|__|/  |_  __________\\__    ___/___   _______/  |_ ",
                " |    __)_  / __ | |  \\   \\/\\/   /\\_  __ \\  \\   __\\/ __ \\_  __ \\|    |_/ __ \\ /  ___/\\   __\\",
                " |        \\/ /_/ | |  |\\        /  |  | \\/  ||  | \\  ___/|  | \\/|    |\\  ___/ \\___ \\  |  |  ",
                "/_______  /\\____ | |__| \\__/\\  /   |__|  |__||__|  \\___  >__|   |____| \\___  >____  > |__|  ",
                "        \\/      \\/           \\/                        \\/                  \\/     \\/        "};

        for (String ascii : asciiArt) {

            LOG.info(ascii);
        }
    }

    @Test
    public void DiagnosticCheckTest() {

        LOG.info("Compare generated with static message check");

        assertEquals(true, CompareMessage.diagnosticCheck());
    }

    @Test
    public void testTruncate() {

        LOG.info("Test truncate");

        final String DATA_REF = "UNB+UNOC:2+CCIS:X:MGK+NLKELA:X:MGK+141112:0708+23714++APERAK'";
        final String DATA_TST = "UNB+UNOC:2+CCIS:X:MGK+NLKELA:X:MGK+141112:0708::::::::::+23714++APERAK++++++++'";

        LOG.info(String.format("Tst input: %s", DATA_TST));
        LOG.info(String.format("Ref input: %s", DATA_REF));

        assertEquals(DATA_REF, EdiUtils.truncateString(DATA_TST, new EDIFACTSettings()));
    }

    @Test
    public void testWriteMedrpt() {

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

        unb.InterchangeControlReference_0020 = "23714";
        unb.ApplicationReference_0026 = "APERAK";

        UNH unh = ediW.record(new UNH());
        unh.MessageReferenceNumber_0062 = "23714";

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
        unt.MessageReferenceNumber_0062 = "23714";

        UNZ unz = ediW.record(new UNZ());
        unz.InterchangeControlCount_0036 = "1";
        unz.InterchangeControlReference_0020 = "23714";

        String ediString = ediW.getEdiMessage();

        final String expectedEdifact =
            "UNA:+.? 'UNB+UNOC:2+CCIS:X:MGK+NLKELA:X:MGK+141112:0708+23714+APERAK'UNH+23714+APERAK:95B:D:UN:BLL001'BMG+997'RFF+Z01:R12064331'UNT+4+23714'UNZ+1+23714'";

        LOG.debug("Transformed EDI {}", ediString);

        CompareMessage cm = new CompareMessage(expectedEdifact, ediString);
        int compareMessage;

        try {
            compareMessage = cm.compareMessage();

            for (String diffEntry : cm.getMessageLog()) {

                LOG.info(diffEntry);
            }
            if (compareMessage != 0) {
                fail();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
