package no.entra.bacnet.agent.devices;

import no.entra.bacnet.json.Bacnet2Json;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static java.nio.file.StandardOpenOption.*;
import static org.slf4j.LoggerFactory.getLogger;

public class DeviceRegistryManualTest {
    private static final Logger log = getLogger(DeviceRegistryManualTest.class);
    private final File hexStringFile;
    private final Path writeToFile;


    public DeviceRegistryManualTest(File hexStringFile, File writeToFile) {
        if (hexStringFile == null || !hexStringFile.isFile()) {
            throw new IllegalArgumentException("File is not readable: " + hexStringFile);
        }
        this.hexStringFile = hexStringFile;
        if (writeToFile != null && writeToFile.canWrite()) {
            this.writeToFile = Paths.get(writeToFile.getAbsolutePath());
        } else {
            log.info("{} is missing. Will not write to that file.");
            this.writeToFile = null;
        }
        log.info("Validating {}", hexStringFile);
    }

    void validateTestdata() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(hexStringFile);
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                lineNum++;
                String line = scanner.nextLine();
                try {
                    String json = Bacnet2Json.hexStringToJson(line);
                    appendToJsonFile(json);
                } catch (Exception e) {
                    log.error("Unexpected exception on line: {}. Content: {}", lineNum, line, e);
                }

            }
        } catch (Exception e) {
            log.error("Failed: ", e);
        }
    }

    private void appendToJsonFile(String json) {
        if (writeToFile != null) {
            try {
                Files.writeString(writeToFile, json + "\n", Charset.forName("UTF-8"), CREATE, WRITE, APPEND);
            } catch (IOException e) {
                log.trace("Could not write to {}. Reason: {}", writeToFile, e);
            }
        }
    }

    public static void main(String[] args) {
        String pathname = "testdata/recordedHexString";
        String writeToFile = "testdata/tmp";
        if (System.getProperty("hexFile") != null) {
            pathname = System.getProperty("hexFile");
        }
        if (System.getProperty("writeToFile") != null) {
            writeToFile = System.getProperty("writeToFile");
        }
        File testDataFile = new File(pathname);
        File writeJsonTo = new File(writeToFile);
        DeviceRegistryManualTest validator = new DeviceRegistryManualTest(testDataFile, writeJsonTo);
        validator.validateTestdata();
        log.info("DONE");
    }
}
