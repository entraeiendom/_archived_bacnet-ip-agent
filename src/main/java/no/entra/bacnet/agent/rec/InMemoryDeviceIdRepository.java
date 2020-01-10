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
        List<DeviceId> existingId = deviceIds.stream()
                .filter(deviceId -> newDeviceId.getId().equals(deviceId.getId()))
                .collect(Collectors.toList());
        if (existingId == null || existingId.size() == 0) {
            deviceIds.add(newDeviceId);
        } else {
            throw new IllegalStateException("Repository already exist with an id of: " + newDeviceId.getId() + ". Please do use the update function.");
        }
    }

    @Override
    public List<DeviceId> findFromTfm(String tfmTag) {
        return null;
    }

    @Override
    public List<DeviceId> findFromInstanceNumber(int instanceNumber) {
        return null;
    }

    @Override
    public List<DeviceId> findFromIpAddress(String ipAddress) {
        return null;
    }

    @Override
    public long size() {
        return deviceIds.size();
    }
}
