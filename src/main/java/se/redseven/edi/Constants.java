package se.redseven.edi;

import se.redseven.edi.components.UNA;
import se.redseven.edi.components.UNB;
import se.redseven.edi.components.UNH;
import se.redseven.edi.components.UNT;
import se.redseven.edi.components.UNZ;

public interface Constants {

    public static final String ERROR_ELEMENT_CREATION =
        "Error: Can not create an Edifact element before a segment is created";
    public static final String ERROR_COMPOSITE_CREATION =
        "Error: Can not create an Edifact composite before a segment is created";

    public static final String INTERCHANGE_COUNT = "$InterchangeCount$";
    public static final String INTERCHANGE_CONTROL_REFERENCE = "$InterchangeControlReference$";
    public static final String SEGMENTS_COUNT = "$SegmentsCount$";
    public static final String MESSAGE_REFERENCE = "$MessageReference$";

    public static final String[] TEMPLATE_TEXT = {INTERCHANGE_COUNT, INTERCHANGE_CONTROL_REFERENCE, SEGMENTS_COUNT,
        MESSAGE_REFERENCE};

    public static final String UNB_Interchange_Header = UNB.class.getSimpleName();
    public static final String UNH_Message_Header = UNH.class.getSimpleName();
    public static final String UNT_Message_Trailer = UNT.class.getSimpleName();
    public static final String UNZ_Interchange_Trailer = UNZ.class.getSimpleName();
    public static final String UNA_Segment = UNA.class.getSimpleName();

}