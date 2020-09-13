package no.entra.bacnet.agent.importer;

import no.entra.bacnet.agent.commands.WhoIsCommand;
import no.entra.bacnet.agent.devices.DeviceId;
import no.entra.bacnet.agent.devices.DeviceIdService;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class DeviceImporter {
    private static final Logger log = getLogger(DeviceImporter.class);

    private final DeviceIdService deviceIdService;
    public DeviceImporter(DeviceIdService deviceIdService) {
        this.deviceIdService = deviceIdService;
    }

    public void findDevices() {
        try {
            WhoIsCommand whoIsCommand = new WhoIsCommand();
            whoIsCommand.broadcast();
        } catch (SocketException e) {
            log.info("Failed to find devices. Reason is: {}", e.getMessage());
        } catch (IOException e) {
            log.info("Failed to find devices. Send WhoIs command threw: {}", e.getMessage());
        }
    }

    public void findSensorsAndPropertiesTheDevicesSupports() {
        List<DeviceId> devicesDetected = deviceIdService.allDevices();

    }

    public void findSensorAndPropertiesConfiguration() {

    }

    public void findPresentValueForSensors() {

    }
}
