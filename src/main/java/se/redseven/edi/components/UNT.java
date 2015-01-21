package se.redseven.edi.components;

import se.redseven.edi.meta.annotation.EdiElement;
import se.redseven.edi.meta.annotation.EdiRecord;

/**

Pos     Segment                                                     M/C Rep Repr.

010     0074    Number of segments in a message                     M   1   an..10
020     0062    Message reference number                            M   1   an..14

 */

@EdiRecord(repMin = 1, repMax = 1)
public class UNT extends Record {

    @EdiElement(length = 10, mandatory = true, value = RECORD_COUNT)
    public String NumberOfSegmentsInMessage_0074 = null;
    @EdiElement(length = 14, mandatory = true, value = MESSAGE_REFERENCE)
    public String MessageReferenceNumber_0062 = null;
}
