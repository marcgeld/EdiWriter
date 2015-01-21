package se.redseven.edi;

import static org.junit.Assert.assertEquals;
import static se.redseven.edi.utils.EdiUtils.truncateString;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        LOG.info(String.format("Tst input: %s", DATA_TST));
        LOG.info(String.format("Ref input: %s", DATA_REF));

        assertEquals(DATA_REF, truncateString(DATA_TST));
    }
}
