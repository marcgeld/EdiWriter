package se.redseven.edi.utils;

import se.redseven.edi.EDIFACTSettings;

public class EdiUtils {

    /** @return escaped value */
    public static String escapeValue(EDIFACTSettings ediSettings, String value) {

        final String relCh = String.valueOf(ediSettings.getEdiReleaseCharcter());

        // escape values if needed
        value = value.replace("" + ediSettings.getEdiReleaseCharcter(), relCh + ediSettings.getEdiReleaseCharcter());

        value = value.replace("" + ediSettings.getEdiElementSeparator(), relCh + ediSettings.getEdiElementSeparator());

        value =
            value.replace("" + ediSettings.getEdiCompositeSeparator(), relCh + ediSettings.getEdiCompositeSeparator());

        return value;
    }

}
