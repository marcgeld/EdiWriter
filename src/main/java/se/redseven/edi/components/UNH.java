package se.redseven.edi.components;

import java.util.ArrayList;

import se.redseven.edi.meta.annotation.EdiComposite;
import se.redseven.edi.meta.annotation.EdiElement;
import se.redseven.edi.meta.annotation.EdiRecord;

/**

Pos     Segment                                                     M/C Rep Repr.

010     0062    Message reference number                            M   1   an..14
020     S009    MESSAGE IDENTIFIER                                  M   1
        0065    Message type                                        M       an..6
        0052    Message version number                              M       an..3
        0054    Message release number                              M       an..3
        0051    Controlling agency, coded                           M       an..3
        0057    Association assigned code                           C       an..6
        0110    Code list directory version number                  C       an..6
        0113    Message type sub-function identification            C       an..6
030     0068    Common access reference                             C   1   an..35
040     S010    STATUS OF THE TRANSFER                              C   1
        0070    Sequence of transfers                               M       n..2
        0073    First and last transfer                             C       a1
050     S016    MESSAGE SUBSET IDENTIFICATION                       C   1
        0115    Message subset identification                       M       an..14
        0116    Message subset version number                       C       an..3
        0118    Message subset release number                       C       an..3
        0051    Controlling agency, coded                           C       an..3
060     S017    MESSAGE IMPLEMENTATION GUIDELINE IDENTIFICATION     C   1
        0121    Message implementation guideline identification     M       an..14
        0122    Message implementation guideline version number     C       an..3
        0124    Message implementation guideline release number     C       an..3
        0051    Controlling agency, coded                           C       an..3
070     S018    SCENARIO IDENTIFICATION                             C   1
        0127    Scenario identification                             M       an..14
        0128    Scenario version number                             C       an..3
        0130    Scenario release number                             C       an..3
        0051    Controlling agency, coded                           C       an..3

 */
@EdiRecord(repMin = 1, repMax = 1)
public class UNH extends Record {

    @EdiElement(length = 14, mandatory = true, value = MESSAGE_REFERENCE)
    public String MessageReferenceNumber_0062 = null;

    // MESSAGE IDENTIFIER
    @EdiComposite(repMin = 1, repMax = 1)
    public ArrayList<S009> S009 = new ArrayList<S009>();

    public class S009 extends Composite {
        @EdiElement(length = 6, mandatory = true)
        public String MessageType_0065 = null;
        @EdiElement(length = 3, mandatory = true)
        public String MessageVersionNumber_0052 = null;
        @EdiElement(length = 3, mandatory = true)
        public String MessageReleaseNumber_0054 = null;
        @EdiElement(length = 3, mandatory = true)
        public String ControllingAgencyCoded_0051 = null;
        @EdiElement(length = 6, mandatory = false)
        public String AssociationAssignedCode_0057 = null;
        @EdiElement(length = 6, mandatory = false)
        public String CodeListDirectoryVersioNnumber_0110 = null;
        @EdiElement(length = 6, mandatory = false)
        public String MessageTypeSubFunctionIdentification_0113 = null;
    }

    // Common access reference
    @EdiComposite(repMin = 0, repMax = 1)
    public ArrayList<S0068> S0068 = new ArrayList<S0068>();

    public class S0068 extends Composite {
        @EdiElement(length = 35, mandatory = false)
        public String CommonAccessReference_0068 = null;
    }

    // STATUS OF THE TRANSFER
    @EdiComposite(repMin = 0, repMax = 1)
    public ArrayList<S010> S010 = new ArrayList<S010>();

    public class S010 extends Composite {
        @EdiElement(length = 2, mandatory = true)
        public String SequenceOfTransfers_0070 = null;
        @EdiElement(length = 1, mandatory = true)
        public String FirstAndLastTransfer_0073 = null;
    }

    // MESSAGE SUBSET IDENTIFICATION
    @EdiComposite(repMin = 0, repMax = 1)
    public ArrayList<S016> S016 = new ArrayList<S016>();

    public class S016 extends Composite {
        @EdiElement(length = 14, mandatory = true)
        public String MessageSubsetIdentification_0115 = null;
        @EdiElement(length = 3, mandatory = false)
        public String MessageSubsetVersionNumber_0116 = null;
        @EdiElement(length = 3, mandatory = false)
        public String MessageSubsetVersionNumber_0118 = null;
    }

    // MESSAGE IMPLEMENTATION GUIDELINE IDENTIFICATION
    @EdiComposite(repMin = 0, repMax = 1)
    public ArrayList<S017> S017 = new ArrayList<S017>();

    public class S017 extends Composite {
        @EdiElement(length = 14, mandatory = true)
        public String MessageImplementationGuidelineIdentification_0121 = null;
        @EdiElement(length = 3, mandatory = false)
        public String MessageImplementationGuidelineVersionNumber_0122 = null;
        @EdiElement(length = 3, mandatory = false)
        public String MessageImplementationGuidelineReleaseNumber_0124 = null;
        @EdiElement(length = 3, mandatory = false)
        public String ControllingAgencyCoded_0051 = null;
    }

    // SCENARIO IDENTIFICATION
    @EdiComposite(repMin = 0, repMax = 1)
    public ArrayList<S018> S018 = new ArrayList<S018>();

    public class S018 extends Composite {
        @EdiElement(length = 14, mandatory = true)
        public String ScenarioIdentification_0127 = null;
        @EdiElement(length = 3, mandatory = false)
        public String ScenarioVersionNumber_0128 = null;
        @EdiElement(length = 3, mandatory = false)
        public String ScenarioReleaseNumber_0130 = null;
        @EdiElement(length = 3, mandatory = false)
        public String ControllingAgencyCoded_0051 = null;
    }
}
