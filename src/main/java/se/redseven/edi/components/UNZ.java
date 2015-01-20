package se.redseven.edi.components;

import se.redseven.edi.meta.annotation.EdiElement;
import se.redseven.edi.meta.annotation.EdiRecord;

/**

Pos     Segment                                                     M/C Rep Repr.

010     0036    Interchange control count                           M   1   n..6
020     0020    Interchange control reference                       M   1   an..14

 */

@EdiRecord(repMin = 1, repMax = 1)
public class UNZ extends Record {

    @EdiElement(length = 6, mandatory = true, value = INTERCHANGE_COUNT)
    public String InterchangeControlCount_0036 = null;
    @EdiElement(length = 14, mandatory = true, value = INTERCHANGE_CONTROL_REFERENCE)
    public String InterchangeControlReference_0020 = null;

}
