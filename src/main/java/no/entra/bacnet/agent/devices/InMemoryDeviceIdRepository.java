package no.entra.bacnet.agent.devices;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static no.entra.bacnet.json.utils.StringUtils.hasValue;
import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryDeviceIdRepository implements DeviceIdRepository {
    private static final Logger log = getLogger(InMemoryDeviceIdRepository.class);

    List<DeviceId> deviceIds = new ArrayList<>();

    @Override
    public void add(DeviceId newDeviceId) {
        List<DeviceId> foundIds = deviceIds.stream()
                .filter(deviceId -> newDeviceId.getId().equals(deviceId.getId()))
                .collect(Collectors.toList());
        if (foundIds == null || foundIds.size() == 0) {
            deviceIds.add(newDeviceId);
        } else {
            throw new IllegalStateException("Repository already exist with an id of: " + newDeviceId.getId() + ". Please do use the update function.");
        }
    }

    //Warn This part of the code is not thread safe.
    //Simultaneous add() or remove() might temper with your replace().
    @Override
    public boolean replace(DeviceId deviceId) {
        boolean replaced = false;
        if (deviceId != null && hasValue(deviceId.getId())) {
            DeviceId existing = get(deviceId.getId());
            if (existing != null) {
                int i = 0;
                for (DeviceId id : deviceIds) {
                    if (id.getId().equals(deviceId.getId())) {
                        deviceIds.set(i, deviceId);
                        replaced = true;
                        break;
                    } else {
                        i++;
                    }
                }
            }
        } else {
            replaced = true;
        }
        return replaced;
    }

    @Override
    public List<DeviceId> allDevices() {
        return deviceIds;
    }

    @Override
    public DeviceId get(String id) {
        DeviceId foundId = null;
        if (hasValue(id)) {
            for (DeviceId deviceId : deviceIds) {
                if (id.equals(deviceId.getId())) {
                   foundId = deviceId;
                   break;
                }
            }
        }
        return foundId;
    }

    @Override
    public List<DeviceId> findFromTfm(String tfmTag) {
        List<DeviceId> foundIds = deviceIds.stream()
                .filter(deviceId -> tfmTag.equals(deviceId.getTfmTag()))
                .collect(Collectors.toList());
        return foundIds;
    }

    @Override
    public List<DeviceId> findFromInstanceNumber(int instanceNumber) {
        List<DeviceId> foundIds = deviceIds.stream()
                .filter(deviceId -> instanceNumber == deviceId.getInstanceNumber())
                .collect(Collectors.toList());
        return foundIds;
    }

    @Override
    public List<DeviceId> findFromIpAddress(String ipAddress) {
        List<DeviceId> foundIds = deviceIds.stream()
                .filter(deviceId -> ipAddress.equals(deviceId.getIpAddress()))
                .collect(Collectors.toList());
        return foundIds;
    }

    @Override
    public long size() {
        return deviceIds.size();
    }
}
