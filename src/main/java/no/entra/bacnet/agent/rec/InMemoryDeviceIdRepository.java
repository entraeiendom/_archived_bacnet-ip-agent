package no.entra.bacnet.agent.rec;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
