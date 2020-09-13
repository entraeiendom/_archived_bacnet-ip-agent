package no.entra.bacnet.agent.importer;

import no.entra.bacnet.agent.devices.DeviceIdService;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class DeviceImporter {
    private static final Logger log = getLogger(DeviceImporter.class);

    private final DeviceIdService deviceIdService;
    public DeviceImporter(DeviceIdService deviceIdService) {
        this.deviceIdService = deviceIdService;
    }

    public void findDevices() {

    }

    public void findSensorsAndPropertiesTheDevicesSupports() {

    }

    public void findSensorAndPropertiesConfiguration() {

    }

    public void findPresentValueForSensors() {

    }
}
