package se.redseven.ediwriter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.ediwriter.EDIFACTSettings;
import se.redseven.ediwriter.error.ParserException;

public class UNARecordParser {

    private static final Logger LOG = LoggerFactory.getLogger(UNARecordParser.class);

    public static EDIFACTSettings parseEdiString(String EdiString) throws ParserException {

        String strUNA = "";

        if (null == EdiString || EdiString.length() < 8) {

            throw new ParserException("UNA is not found in string");
        }

        strUNA = EdiString.substring(0, 8);
        LOG.debug(String.format("UNA: [%s]", strUNA));

        EDIFACTSettings ediSettings = new EDIFACTSettings();

        return ediSettings;
    }
}
