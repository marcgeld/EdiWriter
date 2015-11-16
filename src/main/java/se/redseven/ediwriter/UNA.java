package se.redseven.ediwriter;

import se.redseven.ediwriter.meta.annotation.EdiElement;
import se.redseven.ediwriter.meta.annotation.EdiRecord;

/**
 * UNA EdiRecord.
 * @author ICC.
 */
@EdiRecord(repMin = 0, repMax = 1)
public class UNA extends Segment {

    //TODO Checkstyle varning, rätta till vid tillfälle.
    @EdiElement(length = 1, mandatory = true)
    public String CompositeSeparator = null;
    @EdiElement(length = 1, mandatory = true)
    public String ElementSeparator = null;
    @EdiElement(length = 1, mandatory = true)
    public String DecimalNotation = null;
    @EdiElement(length = 1, mandatory = true)
    public String ReleaseCharcter = null;
    @EdiElement(length = 1, mandatory = true)
    public String _reserved_for_future_use = " ";
    @EdiElement(length = 1, mandatory = true)
    public String RecordSeparator = null;

    /**
     * UNA Segment.
     * @param edifactSettings settings.
     */
    public UNA(EDIFACTSettings edifactSettings) {

        // the defaults
        CompositeSeparator = "" + edifactSettings.getCompositeSeparator();
        ElementSeparator = "" + edifactSettings.getElementSeparator();
        DecimalNotation = "" + edifactSettings.getDecimalNotation();
        ReleaseCharcter = "" + edifactSettings.getReleaseCharcter();
        _reserved_for_future_use = " ";
        RecordSeparator = "" + edifactSettings.getRecordSeparator();
    }

    /**
     * Get the EDIFACTSettings.
     * @return the EDIFACTSettings
     */
    public EDIFACTSettings getEDIFACTSettings() {
        EDIFACTSettings edifactSettings = new EDIFACTSettings();

        edifactSettings.setCompositeSeparator(firstCharFromString(CompositeSeparator));
        edifactSettings.setElementSeparator(firstCharFromString(ElementSeparator));
        edifactSettings.setDecimalNotation(firstCharFromString(DecimalNotation));
        edifactSettings.setReleaseCharcter(firstCharFromString(ReleaseCharcter));
        edifactSettings.setRecordSeparator(firstCharFromString(RecordSeparator));

        return edifactSettings;
    }

    private char firstCharFromString(String value) {

        if (value != null && value.length() > 0) {

            return value.charAt(0);
        }

        return '\0';
    }

    /**
     * Get the UNA Record as string.
     * @return UNA Record as string.
     */
    public String getUNARecord() {

        EDIFACTSettings edifactSettings = getEDIFACTSettings();

        return String.format("UNA%c%c%c%c%c%c", edifactSettings.getCompositeSeparator(),
            edifactSettings.getElementSeparator(), edifactSettings.getDecimalNotation(),
            edifactSettings.getReleaseCharcter(), firstCharFromString(_reserved_for_future_use),
            edifactSettings.getRecordSeparator());
    }
}
