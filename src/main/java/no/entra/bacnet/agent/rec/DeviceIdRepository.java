package no.entra.bacnet.agent.rec;

import java.util.List;

public interface DeviceIdRepository {

    void add(DeviceId deviceId);
    List<DeviceId> findFromTfm(String tfmTag);
    List<DeviceId> findFromInstanceNumber(int instanceNumber);
    List<DeviceId> findFromIpAddress(String ipAddress);

    /**
     * Number of DeviceId's in this Repository.
     * @return long number.
     */
    long size();



}
