package no.entra.bacnet.agent.rec;

import no.entra.bacnet.agent.RealEstateCoreMessage;
import no.entra.bacnet.agent.recording.BacnetHexStringRecorder;
import no.entra.bacnet.agent.recording.FileBacnetHexStringRecorder;
import no.entra.bacnet.json.Bacnet2Json;
import no.entra.bacnet.rec.Bacnet2Rec;
import no.entra.bacnet.rec.RealEstateCore;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

public class ProcessRecordedFile implements Bacnet2RealEstateCore {
    private static final Logger log = getLogger(ProcessRecordedFile.class);

    private final File recordingsFile;
    private final Path recordingsPath;
    private final BacnetHexStringRecorder recorder;


    public ProcessRecordedFile(File recordingsFile) {
        this.recordingsFile = recordingsFile;
        recordingsPath = Paths.get(recordingsFile.getAbsolutePath());
        recorder = new FileBacnetHexStringRecorder(recordingsFile);
    }

    @Override
    public RealEstateCoreMessage buildFromBacnetJson(String bacnetJson) {
        return null;
    }

    public List<RealEstateCore> fetchFromFile() {

        List<RealEstateCore> messages = new ArrayList<>();
        Stream<String> bacnetHexStream = recorder.read();
        List<String> bacnetHexStrings = bacnetHexStream.collect(Collectors.toList());
        ArrayList<String> arrayList = new ArrayList<>(bacnetHexStrings);
        for (String hexString : arrayList) {
            RealEstateCore message = buildRecMessage(hexString);
            if (message != null) {
                messages.add(message);
            }
        }
        return messages;
    }

    protected RealEstateCore buildRecMessage(String hexString) {
        RealEstateCore message = null;
        try {
            String bacnetJson = Bacnet2Json.hexStringToJson(hexString);
            log.trace("Json {}, \nfrom hexString {}", bacnetJson, hexString);
            if (bacnetJson != null) {
                message = Bacnet2Rec.bacnetToRec(bacnetJson);
                log.trace("RealEstateCore message {},\nfrom bacnetJson {}", message, bacnetJson);
            }
        } catch (IllegalArgumentException e) {
            log.debug("Failed to build json from {}. Reason: {}", hexString, e.getMessage());
        } catch (Exception e) {
            log.debug("Failed to create message from {}. Reason: {}", hexString, e.getMessage());
        }

        return message;
    }
}
