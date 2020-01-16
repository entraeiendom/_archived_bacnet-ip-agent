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

    @Test
    public void gatwayIsValid() {
        DeviceId deviceId = new DeviceId();
        assertFalse(deviceId.isValid());
        deviceId.setGatewayInstanceNumber(2000);
        assertFalse(deviceId.isValid());
        deviceId.setGatewayDeviceId(11);
        assertTrue(deviceId.isValid());
        deviceId.setGatewayInstanceNumber(null);
        assertFalse(deviceId.isValid());
    }

    @Test
    public void testEquals() {
        String id = "id1234";
        String tfmTag = "tfm1234";
        Integer instanceNumber = 2002;
        DeviceId originalId = new DeviceId();
        originalId.setId(id);
        DeviceId equalsId = new DeviceId();
        equalsId.setId(id);
        assertTrue(originalId.equals(equalsId));
        //TFM only
        originalId.setId(null);
        originalId.setTfmTag(tfmTag);
        equalsId.setId(null);
        assertFalse(originalId.equals(equalsId));
        equalsId.setTfmTag(tfmTag);
        assertTrue(originalId.equals(equalsId));

        //InstanceNumber
        originalId.setId(id);
        originalId.setInstanceNumber(instanceNumber);
        equalsId.setId(id);
        equalsId.setInstanceNumber(instanceNumber);
        assertTrue(originalId.equals(equalsId));
    }

    @Test
    public void testIsIntegerEqual() {
        DeviceId deviceId = new DeviceId();
        assertTrue( deviceId.isIntegerEqual(100, 100));
        assertTrue( deviceId.isIntegerEqual(null, null));
        assertFalse( deviceId.isIntegerEqual(100, 99));
        assertFalse( deviceId.isIntegerEqual(null, 99));
        assertFalse( deviceId.isIntegerEqual(100, null));
    }

    @Test
    public void testIsStringEqual() {
        DeviceId deviceId = new DeviceId();
        assertTrue( deviceId.isStringEqual("ab", "ab"));
        assertTrue( deviceId.isStringEqual(null, null));
        assertFalse( deviceId.isStringEqual("ab","abc"));
        assertFalse( deviceId.isStringEqual(null, "abc"));
        assertFalse( deviceId.isStringEqual("ab", null));
    }
}