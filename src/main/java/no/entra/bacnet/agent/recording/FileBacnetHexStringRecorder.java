package no.entra.bacnet.agent.recording;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class FileBacnetHexStringRecorder implements BacnetHexStringRecorder {

    private final File recordingFile;

    public FileBacnetHexStringRecorder(File recordingFile) {
        this.recordingFile = recordingFile;
    }

    @Override
    public void persist(String hexString) {
        throw new IllegalArgumentException("Need to be implemented.");
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
