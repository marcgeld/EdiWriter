package se.redseven.edi;

public class EDIFACTSettings {

    protected boolean truncate = true;
    private final static String ZEROES = "00";

    // EDIFACT segments
    /** You use this segment to indicate the end of the current segment and the start of a new segment. The default character for this segment is an apostrophe ('). */
    protected char segmentSeparator = '\'';
    /** This element acts as a data element delimiter. The default character for this segment is a plus sign (+). */
    protected char elementSeparator = '+';
    /** This element acts as a composite element delimiter. The default character for this segment is a colon (:). */
    protected char compositeSeparator = ':';
    /** This character is used to indicate that the text following contains one of the characters used as a composite, data, or segment separator. Therefore, this character is released from its conventional usage in this instance. The default character for this segment is a question mark (?). */
    protected char releaseCharcter = '?';
    /** In this segment, insert a space where all valid standard codes are used. The default character for this segment is a space. */
    protected char padCharcter = ' ';
    /** The recipient ignores the character transferred in this position. The default character for this segment is a full stop/period (.). This segment ensures upward compatibility with earlier versions of the syntax. */
    protected char decimalNotation = ',';

    public EDIFACTSettings() {

        this.truncate = true;
    }

    /** @return is truncating EDI (Remove empty elements and composites) */
    public boolean isTruncating() {

        return truncate;
    }

    /** @param set truncating EDI (Remove empty elements and composites)  */
    public void setTruncating(boolean truncate) {
        this.truncate = truncate;
    }

    /** @return the ediSegmentSeparator */
    public char getEdiSegmentSeparator() {
        return segmentSeparator;
    }

    /** @param ediSegmentSeparator the ediSegmentSeparator to set */
    public void setEdiSegmentSeparator(char ediSegmentSeparator) {
        this.segmentSeparator = ediSegmentSeparator;
    }

    /** @return the ediElementSeparator */
    public char getEdiElementSeparator() {
        return elementSeparator;
    }

    /** @param ediElementSeparator the ediElementSeparator to set */
    public void setEdiElementSeparator(char ediElementSeparator) {
        this.elementSeparator = ediElementSeparator;
    }

    /** @return the ediCompositeSeparator */
    public char getEdiCompositeSeparator() {
        return compositeSeparator;
    }

    /** @param ediCompositeSeparator the ediCompositeSeparator to set */
    public void setEdiCompositeSeparator(char ediCompositeSeparator) {
        this.compositeSeparator = ediCompositeSeparator;
    }

    /** @return the ediReleaseCharcter */
    public char getEdiReleaseCharcter() {
        return releaseCharcter;
    }

    /** @param ediReleaseCharcter the ediReleaseCharcter to set */
    public void setEdiReleaseCharcter(char ediReleaseCharcter) {
        this.releaseCharcter = ediReleaseCharcter;
    }

    /** @return the ediPadCharcter */
    public char getEdiPadCharcter() {
        return padCharcter;
    }

    /** @param ediPadCharcter the ediPadCharcter to set */
    public void setEdiPadCharcter(char ediPadCharcter) {
        this.padCharcter = ediPadCharcter;
    }

    /** @return the ediDecimalNotation */
    public char getEdiDecimalNotation() {
        return decimalNotation;
    }

    /** @param ediDecimalNotation the ediDecimalNotation to set */
    public void setEdiDecimalNotation(char ediDecimalNotation) {
        this.decimalNotation = ediDecimalNotation;
    }

    /**
     * Get the characters in UNA order
     * @return
     */
    public String getSegmentDataUNAOrder() {

        return String.format("%c%c%c%c%c%c", segmentSeparator + elementSeparator + decimalNotation
            + releaseCharcter + padCharcter + segmentSeparator);
    }

    /**
     * Escapes the string.
     * ( Add release char in front of SegmentSeparator, ElementSeparator, CompositeSeparator ReleaseCharcter )
     * @param string to escape
     * @return the escaped string
     */
    public String escapeString(String value) {

        value = value.replace("" + releaseCharcter, "" + releaseCharcter + releaseCharcter);
        value = value.replace("" + segmentSeparator, "" + releaseCharcter + segmentSeparator);
        value = value.replace("" + elementSeparator, "" + releaseCharcter + elementSeparator);
        value = value.replace("" + compositeSeparator, "" + releaseCharcter + compositeSeparator);

        return value;
    }

    private String getTextRepr(String name, char separator) {

        String sepHex = Integer.toHexString(separator);
        return String.format("%s: %c (0x%s)", name, separator, ZEROES.substring(sepHex.length()) + sepHex);
    }

    @Override
    public String toString() {

        return String.format("%s %s %s %s %s %s", getTextRepr("SegmentSeparator", segmentSeparator),
            getTextRepr("ElementSeparator", elementSeparator),
            getTextRepr("CompositeSeparator", compositeSeparator),
            getTextRepr("ReleaseCharcter", releaseCharcter), getTextRepr("PadCharcter", padCharcter),
            getTextRepr("DecimalNotation", decimalNotation));
    }
}
