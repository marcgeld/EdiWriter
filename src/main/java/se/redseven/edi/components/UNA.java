package se.redseven.edi.components;

import se.redseven.edi.EDIFACTSettings;
import se.redseven.edi.meta.annotation.EdiElement;
import se.redseven.edi.meta.annotation.EdiRecord;

@EdiRecord(repMin = 0, repMax = 1)
public class UNA extends Record {

    public UNA(EDIFACTSettings ediSettings) {

        // the defaults
        CompositeSeparator = "" + ediSettings.getEdiCompositeSeparator();
        ElementSeparator = "" + ediSettings.getEdiElementSeparator();
        DecimalNotation = "" + ediSettings.getEdiDecimalNotation();
        ReleaseCharcter = "" + ediSettings.getEdiReleaseCharcter();
        _reserved_for_future_use = " ";
        SegmentSeparator = "" + ediSettings.getEdiSegmentSeparator();
    }

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
    public String SegmentSeparator = null;
}
