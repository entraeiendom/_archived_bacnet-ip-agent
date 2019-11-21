package no.entra.bacnet.agent.recording;

import java.util.stream.Stream;

public interface BacnetHexStringRecorder {
    void persist(String hexString);
    Stream<String> read();
}
