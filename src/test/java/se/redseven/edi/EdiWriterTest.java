package se.redseven.edi;

import static org.junit.Assert.assertEquals;
import static se.redseven.edi.utils.CompareMessage.diagnosticCheck;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Unit test for simple EdiWriter */
public class EdiWriterTest {

    private static final Logger LOG = LoggerFactory.getLogger(EdiWriterTest.class);

    @Test
    public void DiagnosticCheckTest() {

        LOG.info("Compare genarted with static message check");

        assertEquals(true, diagnosticCheck());
    }
}
