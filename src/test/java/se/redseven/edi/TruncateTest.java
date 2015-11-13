package se.redseven.edi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.ediwriter.EDIFACTSettings;
import se.redseven.ediwriter.utils.EdiUtils;

/**
 * Test truncating method.
 *
 */
public class TruncateTest {

    private static final Logger LOG = LoggerFactory.getLogger(TruncateTest.class);

    private static final String DATA_REF = "UNB+UNOC:2+CCIS:X:MGK+NLKELA:X:MGK+141112:0708+23714++APERAK'";
    private static final String DATA_TST =
        "UNB+UNOC:2+CCIS:X:MGK+NLKELA:X:MGK+141112:0708::::::::::+23714++APERAK++++++++'";

    @Test
    public void testTruncate() {

        EDIFACTSettings edifactSettings = new EDIFACTSettings();

        LOG.info(String.format("Tst input: %s", DATA_TST));
        LOG.info(String.format("Ref input: %s", DATA_REF));

        assertEquals(DATA_REF, EdiUtils.truncateString(DATA_TST, edifactSettings));
    }
}
