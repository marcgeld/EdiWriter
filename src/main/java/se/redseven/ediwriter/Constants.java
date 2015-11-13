package se.redseven.ediwriter;

/**
 * Def. av konstanter.
 * @author ICC
 */
//TODO Checkstyle varning, rätta till vid tillfälle.
public interface Constants {

    String ERROR_ELEMENT_CREATION = "Error: Can not create an Edifact element before a record is created";
    String ERROR_COMPOSITE_CREATION = "Error: Can not create an Edifact composite before a record is created";

    String INTERCHANGE_COUNT = "${InterchangeCount}";
    String INTERCHANGE_CONTROL_REFERENCE = "${InterchangeControlReference}";
    String RECORD_COUNT = "${RecordCount}";
    String MESSAGE_REFERENCE = "${MessageReference}";

    String[] TEMPLATE_TEXT = {INTERCHANGE_COUNT, INTERCHANGE_CONTROL_REFERENCE, RECORD_COUNT, MESSAGE_REFERENCE};

    String UNB_INTERCHANGEHEADER = UNB.class.getSimpleName();
    String UNH_MESSAGE_HEADER = UNH.class.getSimpleName();
    String UNT_MESSAGE_TRAILER = UNT.class.getSimpleName();
    String UNZ_INTERCHANGE_TRAILER = UNZ.class.getSimpleName();
    String UNA_RECORD = UNA.class.getSimpleName();

    String SPLIT_REGEXP_SANS_SEP_CHAR = "(?<!\\?)";

}
