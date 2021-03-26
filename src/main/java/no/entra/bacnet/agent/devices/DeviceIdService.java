package no.entra.bacnet.agent.devices;

import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;

import static no.entra.bacnet.json.utils.StringUtils.hasValue;
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
    public synchronized DeviceId add(DeviceId deviceId) {
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

    public synchronized List<DeviceId> allDevices() {
        return idRepository.allDevices();
    }

    public DeviceId createDeviceIdFromTfm(String tfmTag) {
        DeviceId deviceId = new DeviceId();
        deviceId.setTfmTag(tfmTag);
        deviceId = add(deviceId);
        return deviceId;
    }

    public DeviceId createDeviceId(Integer instanceNumber, String ipAddress, String tfmTag) {
        DeviceId deviceId = null;
        if (instanceNumber != null || hasValue(ipAddress) || hasValue(tfmTag)) {
            deviceId = new DeviceId();
            deviceId.setInstanceNumber(instanceNumber);
            deviceId.setIpAddress(ipAddress);
            deviceId.setTfmTag(tfmTag);
            deviceId = add(deviceId);
        }
        return deviceId;
    }

    public List<DeviceId> findMatching(DeviceId deviceId) {
        List<DeviceId> matcingIds = null;
        if (deviceId != null && hasValue(deviceId.getTfmTag())) {
            String tfmTag = deviceId.getTfmTag();
            matcingIds = idRepository.findFromTfm(tfmTag);
        }
        return matcingIds;
    }
}
