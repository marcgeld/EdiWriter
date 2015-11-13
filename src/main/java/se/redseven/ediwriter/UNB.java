package se.redseven.ediwriter;

import java.util.ArrayList;

import se.redseven.ediwriter.meta.annotation.EdiComposite;
import se.redseven.ediwriter.meta.annotation.EdiElement;
import se.redseven.ediwriter.meta.annotation.EdiRecord;

/**
 * UNB EdiRecord.
 * @author ICC.
 *
 * Pos     Segment                                                     M/C Rep Repr.
 *
 * 010     S001    SYNTAX IDENTIFIER                                   M   1
 *         0001    Syntax identifier                                   M       a4
 *         0002    Syntax version number                               M       an1
 *         0080    Service code list directory version number          C       an..6
 *         0133    Character encoding, coded                           C       an..3
 * 020     S002    INTERCHANGE SENDER                                  M   1
 *         0004    Interchange sender identification                   M       an..35
 *         0007    Identification code qualifier                       C       an..4
 *         0008    Interchange sender internal identification          C       an..35
 *         0042    Interchange sender internal sub-identification      C       an..35
 * 030     S003    INTERCHANGE RECIPIENT                               M   1
 *         0010    Interchange recipient identification                M       an..35
 *         0007    Identification code qualifier                       C       an..4
 *         0014    Interchange recipient internal identification       C       an..35
 *         0046    Interchange recipient internal sub-identification   C       an..35
 *         040     S004    DATE AND TIME OF PREPARATION                M   1
 *         0017    Date                                                M       n8
 *         0019    Time                                                M       n4
 *         050     0020    Interchange control reference               M   1   an..14
 * 060     S005    RECIPIENT'S REFERENCE/PASSWORD DETAILS              C   1
 *         0022    Recipient reference/password                        M       an..14
 *         0025    Recipient reference/password qualifier              C       an2
 * 070     0026    Application reference                               C   1   an..14
 * 080     0029    Processing priority code                            C   1   a1
 * 090     0031    Acknowledgement request                             C   1   n1
 * 100     0032    Interchange agreement identifier                    C   1   an..35
 * 110     0035    Test indicator                                      C   1   n1
 *
 *
 */
@EdiRecord(repMin = 0, repMax = 1)
public class UNB extends Record {

    // SYNTAX IDENTIFIER
    @EdiComposite(repMin = 1, repMax = 1)
    // TODO Checkstyle varning, rätta till vid tillfälle.
    public ArrayList<Composite> S001 = new ArrayList<Composite>();

    /**
     * EDIFACT UNB S001 Composite.
     */
    public class S001 extends Composite {

        @EdiElement(length = 4, mandatory = true)
        public String SyntaxIdentifier_0001 = null;
        @EdiElement(length = 1, mandatory = true)
        public String SyntaxVersionNumber_0002 = null;
        @EdiElement(length = 6, mandatory = false)
        public String ServiceCodeListDirectoryVersionNumber_0080 = null;
        @EdiElement(length = 6, mandatory = false)
        public String CharacterEncodingCoded_0133 = null;
    }

    // INTERCHANGE SENDER
    @EdiComposite(repMin = 1, repMax = 1)
    public ArrayList<S002> S002 = new ArrayList<S002>();

    /**
     * EDIFACT UNB S002 Composite.
     */
    public class S002 extends Composite {

        @EdiElement(length = 35, mandatory = true)
        public String InterchangeSenderIdentification_0004;
        @EdiElement(length = 4, mandatory = false)
        public String IdentificationCodeQualifier_0007;
        @EdiElement(length = 35, mandatory = false)
        public String InterchangeSenderInternalIdentification_0008;
        @EdiElement(length = 35, mandatory = false)
        public String InterchangeSenderInternalSubIdentification_0042;
    }

    // INTERCHANGE RECIPIENT
    @EdiComposite(repMin = 1, repMax = 1)
    public ArrayList<S003> S003 = new ArrayList<S003>();

    /**
     * EDIFACT UNB S003 Composite.
     */
    public class S003 extends Composite {

        @EdiElement(length = 35, mandatory = true)
        public String InterchangeRecipientIdentification_0010;
        @EdiElement(length = 4, mandatory = false)
        public String IdentificationCodeQualifier_0007;
        @EdiElement(length = 35, mandatory = false)
        public String InterchangeRecipientInternalIdentification_0014;
        @EdiElement(length = 35, mandatory = false)
        public String InterchangeRecipientInternalSubIdentification_0046;
    }

    // DATE AND TIME OF PREPARATION
    @EdiComposite(repMin = 1, repMax = 1)
    public ArrayList<S004> S004 = new ArrayList<S004>();

    /**
     * EDIFACT UNB S004 Composite.
     */
    public class S004 extends Composite {

        @EdiElement(length = 8, mandatory = true)
        public String Date_0017;
        @EdiElement(length = 4, mandatory = true)
        public String Time_0019;
    }

    @EdiElement(length = 14, mandatory = true, value = INTERCHANGE_CONTROL_REFERENCE)
    public String InterchangeControlReference_0020 = null;

    // RECIPIENT'S REFERENCE/PASSWORD DETAILS
    @EdiComposite(repMin = 0, repMax = 1)
    public ArrayList<S005> S005 = new ArrayList<S005>();

    /**
     * EDIFACT UNB S005 Composite.
     */
    public class S005 extends Composite {

        @EdiElement(length = 14, mandatory = true)
        public String RecipientReferencePassword_0022;
        @EdiElement(length = 2, mandatory = false)
        public String RecipientReferencePasswordQualifier_0022;
    }

    @EdiElement(length = 14, mandatory = false)
    public String ApplicationReference_0026 = null;
    @EdiElement(length = 1, mandatory = false)
    public String ProcessingPriorityCode_0029 = null;
    @EdiElement(length = 1, mandatory = false)
    public String AcknowledgementRequest_0031 = null;
    @EdiElement(length = 35, mandatory = false)
    public String InterchangeAgreementIdentifier_0032 = null;
    @EdiElement(length = 1, mandatory = false)
    public String TestIndicator_0035 = null;

    /**
     * Add new S001 composite.
     * @return the added composite
     */
    public S001 createS001() {
        S001 s001 = new S001();
        S001.add(s001);
        return s001;
    }

    /**
     * Add new S002 composite.
     * @return the added composite.
     */
    public S002 createS002() {
        S002 s002 = new S002();
        S002.add(s002);
        return s002;
    }

    /**
     * Add new S003 composite.
     * @return the added composite.
     */
    public S003 createS003() {
        S003 s003 = new S003();
        S003.add(s003);
        return s003;
    }

    /**
     * Add new S004 composite.
     * @return the added composite.
     */
    public S004 createS004() {
        S004 s004 = new S004();
        S004.add(s004);
        return s004;
    }

    /**
     * Add new S005 composite.
     * @return the added composite.
     */
    public S005 createS005() {
        S005 s005 = new S005();
        S005.add(s005);
        return s005;
    }
}
