package se.redseven.edi;

public class EDIFACTSettings {

    /** is truncating yes / no */
    protected boolean truncate = true;
    /** ZEROES, array of '0' for hex conversion */
    private final static String ZEROES = "00";

    // EDIFACT segments
    /** You use this segment to indicate the end of the current record and the start of a new record. (Default character is an apostrophe "'".) */
    protected char recordSeparator = '\'';
    /** This element acts as a data element delimiter. (Default character is a plus sign "+".) */
    protected char elementSeparator = '+';
    /** This element acts as a composite element delimiter. (Default character is a colon ":".) */
    protected char compositeSeparator = ':';
    /** This character is used to indicate that the text following contains one of the characters used as a composite, data, or segment separator. Therefore, this character is released from its conventional usage in this instance. (Default character is a question mark "?".) */
    protected char releaseCharcter = '?';
    /** In this segment, insert a space where all valid standard codes are used. (Default character is a space " ".) */
    protected char padCharcter = ' ';
    /** The recipient ignores the character transferred in this position. (Default character is a full stop/period/dot ".".)
     ** This segment ensures upward compatibility with earlier versions of the syntax. */
    protected char decimalNotation = ',';

    /**
     * Default constructor for Settings
     */
    public EDIFACTSettings() {

        this.truncate = true;
    }

    /**
     * Is truncate EDIFACT enabled (Remove empty elements and composites)
     * @return is truncate on (true) or off (false).
     */
    public boolean isTruncating() {

        return truncate;
    }

    /**
     * Set truncate EDIFACT (Remove empty elements and composites)
     * @param truncate - set truncate to on (true) or off (false)
     */
    public void setTruncating(boolean truncate) {

        this.truncate = truncate;
    }

    /**
     * Get record separator char.
     * @return teh char that separates records in a message. Usually marks end of line (EOL).
     */
    public char getRecordSeparator() {

        return recordSeparator;
    }

    /**
     * Set record separator char.
     * @param recordSeparator char that separates records in a message. Usually marks end of line (EOL).
     */
    public void setRecordSeparator(char recordSeparator) {

        this.recordSeparator = recordSeparator;
    }

    /**
     * Get element separator char.
     * @return char that separates elements in a message.
     */
    public char getElementSeparator() {

        return elementSeparator;
    }

    /**
     * Set element separator char.
     * @param elementSeparator char that separates elements in a message.
     */
    public void setElementSeparator(char elementSeparator) {

        this.elementSeparator = elementSeparator;
    }

    /**
     * Get composite separator char.
     * @return the char that separates element in a composite.
     */
    public char getCompositeSeparator() {

        return compositeSeparator;
    }

    /**
     * Set composite separator char
     * @param compositeSeparator char that separates element in a composite.
     */
    public void setCompositeSeparator(char compositeSeparator) {

        this.compositeSeparator = compositeSeparator;
    }

    /**
     * Set release char.
     * @return char that escapes the next character in a sequence.
     */
    public char getReleaseCharcter() {

        return releaseCharcter;
    }

    /**
     * Set release char.
     * @param releaseCharcter char that escapes the next character in a sequence.
     */
    public void setReleaseCharcter(char releaseCharcter) {

        this.releaseCharcter = releaseCharcter;
    }

    /**
     * Get pad char.
     * @return char that mark empty space.
     */
    public char getPadCharcter() {

        return padCharcter;
    }

    /**
     * Set pad char.
     * @param padCharcter char that mark empty space.
     */
    public void setPadCharcter(char padCharcter) {

        this.padCharcter = padCharcter;
    }

    /**
     * Get decimal notation (separator) char.
     * @return char that separate the integer part from the fractional part of a number (Decimal mark).
     */
    public char getDecimalNotation() {

        return decimalNotation;
    }

    /**
     * Set decimal notation (separator) char.
     * @param decimalNotation. char that separate the integer part from the fractional part of a number (Decimal mark).
     */
    public void setDecimalNotation(char decimalNotation) {

        this.decimalNotation = decimalNotation;
    }

    /**
     * Get the characters in UNA order.
     * @return returns recordSeparator, elementSeparator, decimalNotation, releaseCharcter, padCharcter and recordSeparator
     * that forms an UNA.
     */
    public String getSegmentDataUNAOrder() {

        return String.format("%c%c%c%c%c%c", recordSeparator + elementSeparator + decimalNotation + releaseCharcter
            + padCharcter + recordSeparator);
    }

    /**
     * Escapes the string.
     * ( Add release char in front of segmentSeparator, elementSeparator, compositeSeparator releaseCharcter )
     * @param value string to escape
     * @return the escaped string
     */
    public String escapeString(String value) {

        value = value.replace("" + releaseCharcter, "" + releaseCharcter + releaseCharcter);
        value = value.replace("" + recordSeparator, "" + releaseCharcter + recordSeparator);
        value = value.replace("" + elementSeparator, "" + releaseCharcter + elementSeparator);
        value = value.replace("" + compositeSeparator, "" + releaseCharcter + compositeSeparator);

        return value;
    }

    /**
     * A string with name and hexadecimal representation of char.
     *
     * @param name Name of charcter to describe.
     * @param charcter character to describe.
     * @return Description string with character and hex value.
     */
    public String getTextRepresentationOfCharacter(String name, char charcter) {

        String sepHex = Integer.toHexString(charcter);
        return String.format("%s: %c (0x%s)", name, charcter, ZEROES.substring(sepHex.length()) + sepHex);
    }

    @Override
    public String toString() {

        return String.format("%s %s %s %s %s %s",
            getTextRepresentationOfCharacter("SegmentSeparator", recordSeparator),
            getTextRepresentationOfCharacter("ElementSeparator", elementSeparator),
            getTextRepresentationOfCharacter("CompositeSeparator", compositeSeparator),
            getTextRepresentationOfCharacter("ReleaseCharcter", releaseCharcter),
            getTextRepresentationOfCharacter("PadCharcter", padCharcter),
            getTextRepresentationOfCharacter("DecimalNotation", decimalNotation));
    }
}
