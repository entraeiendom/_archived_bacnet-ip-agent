package no.entra.bacnet.agent.devices;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DeviceIdTest {

    @Test
    public void isValid() {
        DeviceId deviceId = new DeviceId();
        deviceId.setId("id");
        assertTrue(deviceId.isValid());
        deviceId.setId(null);
        assertFalse(deviceId.isValid());
        assertFalse(deviceId.isValid());
        deviceId.setTfmTag("ttt");
        assertTrue(deviceId.isValid());
        deviceId.setTfmTag(null);
        assertFalse(deviceId.isValid());
        deviceId.setIpAddress("ip");
        assertTrue(deviceId.isValid());
        deviceId.setIpAddress(null);
        assertFalse(deviceId.isValid());
        deviceId.setInstanceNumber(2000);
        assertTrue(deviceId.isValid());
    }
}