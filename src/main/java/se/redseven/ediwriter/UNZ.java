package se.redseven.ediwriter;

import se.redseven.ediwriter.meta.annotation.EdiElement;
import se.redseven.ediwriter.meta.annotation.EdiRecord;

/**

Pos     Segment                                                     M/C Rep Repr.

010     0036    Interchange control count                           M   1   n..6
020     0020    Interchange control reference                       M   1   an..14

 */

@EdiRecord(repMin = 1, repMax = 1)
public class UNZ extends Segment {

    //TODO Checkstyle varning, rätta till vid tillfälle.

    @EdiElement(length = 6, mandatory = true, value = INTERCHANGE_COUNT)
    public String InterchangeControlCount_0036 = null;
    @EdiElement(length = 14, mandatory = true, value = INTERCHANGE_CONTROL_REFERENCE)
    public String InterchangeControlReference_0020 = null;
}
