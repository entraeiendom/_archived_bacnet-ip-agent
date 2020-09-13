package no.entra.bacnet.agent.devices;

import java.util.List;

public interface DeviceIdRepository {

    void add(DeviceId deviceId);
    List<DeviceId> findFromTfm(String tfmTag);
    List<DeviceId> findFromInstanceNumber(int instanceNumber);
    List<DeviceId> findFromIpAddress(String ipAddress);

    /**
     * Update all parameters in a DeviceId
     * @param deviceId
     * @return false if deviceId with same id was not found.
     */
    boolean replace(DeviceId deviceId);

    /**
     * Get a DeviceId by id.
     * @param id the uuid of an Device
     * @return DeviceId if found, null if deviceId do not exist.
     */
    DeviceId get(String id);

    /**
     * Number of DeviceId's in this Repository.
     * @return long number.
     */
    long size();


    List<DeviceId> allDevices();
}
