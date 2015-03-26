package se.redseven.edi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static se.redseven.edi.utils.CompareMessage.diagnosticCheck;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.edi.utils.CompareMessage;

/** Unit test for simple EdiWriter */
/**
 *
 *
 */
public class EdiWriterTest {

    private static final Logger LOG = LoggerFactory.getLogger(EdiWriterTest.class);

    @Test
    public void DiagnosticCheckTest() {

        LOG.info("Compare generated with static message check");

        assertEquals(true, diagnosticCheck());
    }

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

        //System.out.println(ediString);
        final String expectedEdifact =
            "UNA:+.? 'UNB+UNOC:2+CCIS:X:MGK+NLKELA:X:MGK+141112:0708+23714++APERAK'UNH+23714+APERAK:95B:D:UN:BLL001'BMG+997'RFF+Z01:R12064331'UNT+4+23714'UNZ+1+23714'";
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
