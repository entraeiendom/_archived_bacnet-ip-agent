package no.entra.bacnet.agent.devices;

import org.junit.Test;

import static org.junit.Assert.*;

public class BacnetJsonDeviceIdParserTest {

    @Test
    public void parse() {
        String bacnetJson = "{\n" +
                "  \"configurationRequest\": {\n" +
                "    \"observedAt\": \"2020-01-10T12:46:36.087069\",\n" +
                "    \"id\": \"TODO\",\n" +
                "    \"properties\": {\n" +
                "      \"DeviceInstanceRangeHighLimit\": \"1966\",\n" +
                "      \"DeviceInstanceRangeLowLimit\": \"1966\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"sender\": {\n" +
                "    \"ip\": \"127.0.0.1\",\n" +
                "    \"port\": \"47808\"\n," +
                "    \"instanceNumber\": 2002\n" +
                "  },\n" +
                "  \"service \": \"WhoIs \"\n" +
                "}";
        DeviceId deviceId = BacnetJsonDeviceIdParser.parse(bacnetJson);
        assertNotNull(deviceId);
        assertNull(deviceId.getId());
        assertEquals("127.0.0.1", deviceId.getIpAddress());
        assertEquals("47808", deviceId.getPortNumber());
        assertEquals(2002, deviceId.getInstanceNumber().intValue());

    }

    @Test
    public void parseIllegalSender() {
        String bacnetJson = "{\n" +
                "  \"sender\": {\n" +
                "    \"ip\": \"127.0.0.1\",\n" +
                "    \"instanceNumber\": 2002\n" +
                "  }\n" +
                "}";
        DeviceId deviceId = BacnetJsonDeviceIdParser.parse(bacnetJson);
        assertNotNull(deviceId);
        assertNull(deviceId.getId());
        assertEquals("127.0.0.1", deviceId.getIpAddress());
        assertNull( deviceId.getPortNumber());
        assertEquals(2002, deviceId.getInstanceNumber().intValue());
    }

    @Test
    public void parseUnknownSender() {
        String bacnetJson = "{\n" +
                "  \"sender\": \"unknown\"}\n" +
                "}";
        DeviceId deviceId = BacnetJsonDeviceIdParser.parse(bacnetJson);
        assertNotNull(deviceId);
        assertNull(deviceId.getId());
        assertNull(deviceId.getIpAddress());
        assertNull(deviceId.getPortNumber());
        assertNull(deviceId.getInstanceNumber());
    }

    @Test
    public void parseIamRequest() {
        String bacnetJson = "{\"configurationRequest\":{\"observedAt\":\"2020-01-10T12:46:32.021648\",\"id\":\"TODO\",\"properties\":{\"ObjectType\":\"Device\",\"InstanceNumber\":\"2002\",\"MaxADPULengthAccepted\":\"1476\",\"SegmentationSupported\":\"SegmentedBoth\"}},\"sender\":\"unknown\",\"service\":\"IAm\"}";
        DeviceId deviceId = BacnetJsonDeviceIdParser.parse(bacnetJson);
        assertNotNull(deviceId);
        assertEquals(2002, deviceId.getInstanceNumber().intValue());
    }
}