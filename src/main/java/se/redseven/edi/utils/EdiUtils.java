package se.redseven.edi.utils;

import se.redseven.edi.EDIFACTSettings;

/**
 * Utilities for EDI.
 *
 *
 */
public class EdiUtils {

    /**
     * Escape a values according to EDIFACT rules.
     *
     * @param ediSettings - Settings used for EDIFACT
     * @param value - Value to escape.
     * @return escaped value.
     */
    public static String escapeValue(EDIFACTSettings ediSettings, String value) {

        final String relCh = String.valueOf(ediSettings.getReleaseCharcter());

        // escape values if needed
        value = value.replace("" + ediSettings.getReleaseCharcter(), relCh + ediSettings.getReleaseCharcter());

        value = value.replace("" + ediSettings.getElementSeparator(), relCh + ediSettings.getElementSeparator());

        value = value.replace("" + ediSettings.getCompositeSeparator(), relCh + ediSettings.getCompositeSeparator());

        return value;
    }

    /**
     * Truncate EDI-string
     *
     * @param record.
     * @return truncated record.
     */
    public static String truncateString(String record) {

        return record.replaceAll(":+\\+", "+").replaceAll("\\++'", "'");
    }
}
