package no.entra.bacnet.agent.devices;

import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.slf4j.LoggerFactory.getLogger;

public class DeviceIdFromBacnetJsonManualTest {
    private static final Logger log = getLogger(DeviceIdFromBacnetJsonManualTest.class);
    private final File bacnetJsonFile;
    private final Path writeDeviceIdsFile;
    private final DeviceIdService idService;
    private final DeviceIdRepository idRepository;


    public DeviceIdFromBacnetJsonManualTest(File bacnetJsonFile, File writeDeviceIdsFile) {
        if (bacnetJsonFile == null || !bacnetJsonFile.isFile()) {
            throw new IllegalArgumentException("File is not readable: " + bacnetJsonFile);
        }
        this.bacnetJsonFile = bacnetJsonFile;
        if (writeDeviceIdsFile != null && writeDeviceIdsFile.canWrite()) {
            this.writeDeviceIdsFile = Paths.get(writeDeviceIdsFile.getAbsolutePath());
        } else {
            this.writeDeviceIdsFile = null;
        }

        idRepository = new InMemoryDeviceIdRepository();
        idService = new DeviceIdService(idRepository);
        log.info("Validating {}", bacnetJsonFile);
    }

    void validateTestdata() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(bacnetJsonFile);
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                lineNum++;
                String line = scanner.nextLine();
                try {
                    DeviceId deviceId = BacnetJsonDeviceIdParser.parse(line);
                    addToIdRegistry(deviceId);
                } catch (Exception e) {
                    log.error("Unexpected exception on line: {}. Content: {}", lineNum, line, e);
                }

            }
        } catch (Exception e) {
            log.error("Failed: ", e);
        }
    }
    long countDeviceIds() {
        return idRepository.size();
    }

    void addToIdRegistry(DeviceId deviceId) {
          idService.add(deviceId);
    }

    public static void main(String[] args) {
        String pathname = "testdata/recordedBacnetJson";
        String writeToFile = "testdata/deviceIdJson";
        if (System.getProperty("hexFile") != null) {
            pathname = System.getProperty("hexFile");
        }
        if (System.getProperty("writeDeviceIdsFile") != null) {
            writeToFile = System.getProperty("writeDeviceIdsFile");
        }
        File testDataFile = new File(pathname);
        File writeJsonTo = new File(writeToFile);
        DeviceIdFromBacnetJsonManualTest validator = new DeviceIdFromBacnetJsonManualTest(testDataFile, writeJsonTo);
        validator.validateTestdata();
        long numberOfDevicdIds = validator.countDeviceIds();
        log.info("Added {}", numberOfDevicdIds);
        log.info("DONE");
    }
}
