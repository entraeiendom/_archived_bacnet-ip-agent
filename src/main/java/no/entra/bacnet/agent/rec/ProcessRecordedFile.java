package no.entra.bacnet.agent.rec;

import no.entra.bacnet.agent.RealEstateCoreMessage;
import no.entra.bacnet.agent.recording.BacnetHexStringRecorder;
import no.entra.bacnet.agent.recording.FileBacnetHexStringRecorder;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

public class ProcessRecordedFile implements Bacnet2RealEstateCore {
    private static final Logger log = getLogger(ProcessRecordedFile.class);

    private final File recodedFile;
    private final BacnetHexStringRecorder recorder;

    public ProcessRecordedFile(File recodedFile) {
        this.recodedFile = recodedFile;
        recorder = new FileBacnetHexStringRecorder(recodedFile);
    }

    @Override
    public RealEstateCoreMessage buildFromBacnetJson(String bacnetJson) {
        return null;
    }

    public List<RealEstateCoreMessage> fetchFromFile() {

        List<RealEstateCoreMessage> messages = new ArrayList<>();
        Stream<String> bacnetJsonStream = recorder.read();
        List<String> jsonList = bacnetJsonStream.collect(Collectors.toList());
        ArrayList<String> arrayList = new ArrayList<String>(jsonList);
        for (String bacnetJson : arrayList) {
            RealEstateCoreMessage message = buildRecFromJson(bacnetJson);
            if (message != null) {
                messages.add(message);
            }
        }
        return messages;
    }

    private RealEstateCoreMessage buildRecFromJson(String json) {
        return new RealEstateCoreMessage(json);
    }
}
