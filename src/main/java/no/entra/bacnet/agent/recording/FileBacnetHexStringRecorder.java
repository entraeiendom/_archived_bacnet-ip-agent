package no.entra.bacnet.agent.recording;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.*;
import static org.slf4j.LoggerFactory.getLogger;

public class FileBacnetHexStringRecorder implements BacnetHexStringRecorder {
    private static final Logger log = getLogger(FileBacnetHexStringRecorder.class);

    private final File recordingFile;
    private final Path recordingsPath;

    public FileBacnetHexStringRecorder(File recordingFile) {
        this.recordingFile = recordingFile;
        this.recordingsPath = Paths.get(recordingFile.getAbsolutePath());
    }

    @Override
    public void persist(String hexString) {
        log.info("ToRecord;{}", hexString);
        try {
            Files.writeString(recordingsPath, hexString + "\n", Charset.forName("UTF-8"), CREATE, WRITE, APPEND);
        } catch (IOException e) {
            log.trace("Could not write to {}. Reason: {}", recordingFile, e);
        }
    }

    @Override
    public Stream<String> read() {
        Stream<String> stream = null;
        try  {
            stream = Files.lines(recordingFile.toPath());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
