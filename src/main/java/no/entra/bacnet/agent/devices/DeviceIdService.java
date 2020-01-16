package no.entra.bacnet.agent.devices;

import org.slf4j.Logger;

import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

public class DeviceIdService {
    private static final Logger log = getLogger(DeviceIdService.class);

    private final DeviceIdRepository idRepository;

    public DeviceIdService(DeviceIdRepository idRepository) {
        this.idRepository = idRepository;
    }

    /**
     *
     * @param deviceId
     * @return deviceId with Id parameter set.
     */
    public DeviceId add(DeviceId deviceId) {
        if (deviceId != null && deviceId.isValid()) {
            if (deviceId.getId() == null) {
                String id = UUID.randomUUID().toString();
                deviceId.setId(id);
            }
            try {
                idRepository.add(deviceId);
            } catch (IllegalStateException e) {
                log.trace("Could not add new deviceId for {}. A device with this id {} already exist in the repository. " +
                        "Reason: {}", deviceId, deviceId.getId(), e.getMessage());
            }
        }
        return deviceId;
    }

    public DeviceId createDeviceIdFromTfm(String tfmTag) {
        DeviceId deviceId = new DeviceId();
        deviceId.setTfmTag(tfmTag);
        deviceId = add(deviceId);
        return deviceId;
    }

    public DeviceId createDeviceId(int instanceNumber, String ipAddress, String tfmTag) {
        DeviceId deviceId = new DeviceId();
        deviceId.setInstanceNumber(instanceNumber);
        deviceId.setIpAddress(ipAddress);
        deviceId.setTfmTag(tfmTag);
        deviceId = add(deviceId);
        return deviceId;
    }
}
