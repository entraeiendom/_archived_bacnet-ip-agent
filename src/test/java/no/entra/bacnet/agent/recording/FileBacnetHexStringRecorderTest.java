package no.entra.bacnet.agent.recording;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

public class FileBacnetHexStringRecorderTest {
    private static final Logger log = getLogger(FileBacnetHexStringRecorderTest.class);
    File file;

    @Before
    public void setUp() throws Exception {
        String path = "src/test/resources/bacnet-hexstring";

        file = new File(path);
        assertTrue("File should exist: " + path, file.exists());
        String absolutePath = file.getAbsolutePath();
        assertTrue("File should exist: " + absolutePath, file.exists());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void persist() {
    }

    @Test
    public void read() {
        FileBacnetHexStringRecorder recorder = new FileBacnetHexStringRecorder(file);
        Stream<String> hexStrings = recorder.read();
        assertNotNull(hexStrings);
        Optional<String> first = hexStrings.findFirst();
        assertNotNull(first);
        assertTrue(first.isPresent());
        String firstHexString = first.get();
        assertNotNull(firstHexString);
        assertFalse(firstHexString.isEmpty());

    }
}