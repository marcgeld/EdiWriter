package se.redseven.ediwriter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.ediwriter.error.ParserException;

/**
 * Utilities for EDI.
 */
public final class EdiUtils {

    private static final Logger LOG = LoggerFactory.getLogger(EdiUtils.class);

    private EdiUtils() {

    }

    /**
     * Escape a values according to EDIFACT rules.
     *
     * @param ediSettings Settings used for EDIFACT
     * @param value Value to escape.
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
     * Truncate String.
     * @param record Record to truncate.
     * @param settings Settings to use when truncating.
     * @return String truncated according to rules.
     */
    public static String truncateString(String record, EDIFACTSettings settings) {

        if (null == settings) {

            throw new ParserException("EDIFACTSettings is null");
        }

        String outRecord = record;
        String regex = "";
        String elemSepStr = String.format("\\x%02X", settings.getElementSeparator() & 0xff);
        String compSepStr = String.format("\\x%02X", settings.getCompositeSeparator() & 0xff);
        String recSepStr = String.format("\\x%02X", settings.getRecordSeparator() & 0xff);

        // Check for runs of composite separator chars (:) between element separators (+).
        // +:::::::::+' => ++'
        regex = String.format("%s+%s", compSepStr, elemSepStr);
        LOG.debug(String.format("regex string: %s", regex));
        outRecord = outRecord.replaceAll(regex, "" + settings.getElementSeparator());

        // Check for runs of element separator chars (+)  before end of line char (')
        // +++++++++' => '
        regex = String.format("%s+%s", elemSepStr, recSepStr);
        LOG.debug(String.format("regex string: %s", regex));
        outRecord = outRecord.replaceAll(regex, "" + settings.getRecordSeparator());

        // If only record name is left, truncate it!
        if (outRecord.matches("\\A[A-Z]{3}" + settings.getRecordSeparator())) {
            outRecord = "";
        }

        if (LOG.isDebugEnabled() && !record.equalsIgnoreCase(outRecord)) {

            LOG.debug(String.format("(trunc) %s => %s", String.valueOf(record), String.valueOf(outRecord)));
        }

        return outRecord;
    }

    /**
     * Split EDIFACT message on segment ediSegmentSeparator.
     *
     * @param message Message to split
     * @param ediSegmentSeparator character to split on.
     * @return Array with one segment per String.
     */
    public static String[] rowSplit(String message, char ediSegmentSeparator) {

        String[] splittedString = StringUtils.split(message, ediSegmentSeparator);

        for (int i = 0; i < splittedString.length; i++) {

            splittedString[i] = splittedString[i] + ediSegmentSeparator;
        }

        return splittedString;
    }
}
