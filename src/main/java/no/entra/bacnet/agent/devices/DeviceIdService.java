package no.entra.bacnet.agent.devices;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class DeviceIdService {
    private static final Logger log = getLogger(DeviceIdService.class);

    private final DeviceIdRepository idRepository;

    public DeviceIdService(DeviceIdRepository idRepository) {
        this.idRepository = idRepository;
    }

    public DeviceId createDeviceIdFromTfm(String tfmTag) {
        return null;
    }

    public DeviceId createDeviceId(int instanceNumber, String ipAddress, String tfmTag) {
       return null;
    }
}
