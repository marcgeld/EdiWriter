package se.redseven.ediwriter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.ediwriter.EDIFACTSettings;
import se.redseven.ediwriter.error.ParserException;

/**
 * Parser for EDIFACT UNA Record.
 * @author ICC
 */
public final class UNARecordParser {

    private static final Logger LOG = LoggerFactory.getLogger(UNARecordParser.class);

    private UNARecordParser() {

    }

    /**
     * Parse an EDIFACT message (string) to verify that UNA exists and to get values.
     * @param ediString input String.
     * @return Exception when missing UNA.
     * @throws ParserException Exception when UNA is not found.
     */
    public static EDIFACTSettings parseEdiString(String ediString) throws ParserException {

        if (null == ediString || ediString.length() < 8) {

            throw new ParserException("UNA is not found in string");
        }

        EDIFACTSettings ediSettings = new EDIFACTSettings();
        ediSettings.setSegmentUNAFromString(ediString.substring(3, 9));

        return ediSettings;
    }
}
