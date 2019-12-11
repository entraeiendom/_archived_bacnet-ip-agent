package no.entra.bacnet.agent.rec;

import no.entra.bacnet.rec.RealEstateCore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

public class ProcessRecordedFileTest {
    private static final Logger log = getLogger(ProcessRecordedFileTest.class);

    private File file;
    private ProcessRecordedFile processor;

    @Before
    public void setUp() throws Exception {
        String path = "src/test/resources/bacnet-hexstring";

        file = new File(path);
        String absolutePath = file.getAbsolutePath();
        assertTrue(absolutePath.endsWith(path));
        processor = new ProcessRecordedFile(file);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void buildFromBacnetJson() {
    }

    @Test
    public void fetchFromFile() {
        List<RealEstateCore> messages = processor.fetchFromFile();
        assertNotNull(messages);
        log.debug("Found {}", messages.size());
        assertTrue(messages.size() > 0);

    }
}