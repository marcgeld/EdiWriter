package se.redseven.ediwriter;

public interface Constants {

    public static final String ERROR_ELEMENT_CREATION =
        "Error: Can not create an Edifact element before a record is created";
    public static final String ERROR_COMPOSITE_CREATION =
        "Error: Can not create an Edifact composite before a record is created";

    public static final String INTERCHANGE_COUNT = "$InterchangeCount$";
    public static final String INTERCHANGE_CONTROL_REFERENCE = "$InterchangeControlReference$";
    public static final String RECORD_COUNT = "$RecordCount$";
    public static final String MESSAGE_REFERENCE = "$MessageReference$";

    public static final String[] TEMPLATE_TEXT = {INTERCHANGE_COUNT, INTERCHANGE_CONTROL_REFERENCE, RECORD_COUNT,
        MESSAGE_REFERENCE};

    public static final String UNB_Interchange_Header = UNB.class.getSimpleName();
    public static final String UNH_Message_Header = UNH.class.getSimpleName();
    public static final String UNT_Message_Trailer = UNT.class.getSimpleName();
    public static final String UNZ_Interchange_Trailer = UNZ.class.getSimpleName();
    public static final String UNA_Record = UNA.class.getSimpleName();

    public static final String SPLIT_REGEXP_SANS_SEP_CHAR = "(?<!\\?)";

}