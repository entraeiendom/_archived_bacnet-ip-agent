package no.entra.bacnet.agent.rec;

import com.serotonin.bacnet4j.exception.IllegalPduTypeException;
import no.entra.bacnet.agent.RealEstateCoreMessage;
import no.entra.bacnet.agent.recording.BacnetHexStringRecorder;
import no.entra.bacnet.agent.recording.FileBacnetHexStringRecorder;
import no.entra.bacnet.json.BacNetParser;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.*;
import static org.slf4j.LoggerFactory.getLogger;

public class ProcessRecordedFile implements Bacnet2RealEstateCore {
    private static final Logger log = getLogger(ProcessRecordedFile.class);

    private final File recordingsFile;
    private final Path recordingsPath;
    private final BacNetParser bacNetParser;
    private final BacnetHexStringRecorder recorder;

    public ProcessRecordedFile(File recordingsFile) {
        this(recordingsFile, new BacNetParser());
    }

    public ProcessRecordedFile(File recordingsFile, BacNetParser bacNetParser) {
        this.recordingsFile = recordingsFile;
        recordingsPath = Paths.get(recordingsFile.getAbsolutePath());
        recorder = new FileBacnetHexStringRecorder(recordingsFile);
        this.bacNetParser = bacNetParser;
    }

    @Override
    public RealEstateCoreMessage buildFromBacnetJson(String bacnetJson) {
        return null;
    }

    public void writeToFile(String hexString) {
        //TODO #7 move to FileBacnetHexStringRecorder
        log.info("ToRecord;{}", hexString);
        try {
            Files.writeString(recordingsPath, hexString + "\n", Charset.forName("UTF-8"), CREATE, WRITE, APPEND);
        } catch (IOException e) {
            log.trace("Could not write to {}. Reason: {}", recordingsFile, e);
        }
    }

    public List<RealEstateCoreMessage> fetchFromFile() {

        List<RealEstateCoreMessage> messages = new ArrayList<>();
        Stream<String> bacnetHexStream = recorder.read();
        List<String> bacnetHexStrings = bacnetHexStream.collect(Collectors.toList());
        ArrayList<String> arrayList = new ArrayList<>(bacnetHexStrings);
        for (String hexString : arrayList) {
            RealEstateCoreMessage message = buildHexString(hexString);
            if (message != null) {
                messages.add(message);
            }
        }
        return messages;
    }

    private RealEstateCoreMessage buildHexString(String hexString) {
        RealEstateCoreMessage message = new RealEstateCoreMessage(hexString);
        try {
            String apduHexString = findApduHexString(hexString);
            String json = bacNetParser.jasonFromApdu(apduHexString);
            message.setBacknetJson(json);
            log.debug("Did build message from {}", hexString);
        } catch (IllegalArgumentException e) {
            log.debug("Failed to build json from {}. Reason: {}", hexString, e.getMessage());
        } catch (IllegalPduTypeException e) {
            log.debug("Could not build APDU from {}. Reason: {}", hexString, e.getMessage());
        } catch (Exception e) {
            log.debug("Failed to create message from {}. Reason: {}", hexString, e.getMessage());
        }

        return message;
    }

    String findApduHexString(String hexString) {
        String apduHexString = null;
        if (hexString != null && hexString.startsWith("81")) {
            apduHexString = hexString.substring(10);
        } else {
            apduHexString = hexString;
        }
        return apduHexString;
    }


}
