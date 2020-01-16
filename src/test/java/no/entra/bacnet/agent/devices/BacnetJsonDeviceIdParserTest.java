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

    @Test
    public void parseIHaveRequest() {
        String bacnetJson = "{\n" +
                "  \"configurationRequest\": {\n" +
                "    \"observedAt\": \"2020-01-13T14:25:36.433205\",\n" +
                "    \"id\": \"TODO\",\n" +
                "    \"source\": \"0961\",\n" +
                "    \"properties\": {\n" +
                "      \"Request\": \"IHave\",\n" +
                "      \"NotificationClass\": \"0\",\n" +
                "      \"Device\": \"11\",\n" +
                "      \"ObjectName\": \"tfmtag-example\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"sender\": {\n" +
                "    \"gateway\": {\n" +
                "      \"instanceNumber\": 2401,\n" +
                "      \"deviceId\": 11\n" +
                "    }\n" +
                "  },\n" +
                "  \"service\": \"IHave\"\n" +
                "}";
        DeviceId deviceId = BacnetJsonDeviceIdParser.parse(bacnetJson);
        assertNotNull(deviceId);
        assertEquals(2401, deviceId.getGatewayInstanceNumber().intValue());
        assertEquals(11, deviceId.getGatewayDeviceId().intValue());
        assertEquals("tfmtag-example", deviceId.getTfmTag());
    }

    @Test
    public void parseObservation() {
        String bacnetJson = "{\n" +
                "  \"sender\": \"unknown\",\n" +
                "  \"service\": \"GetAlarmSummary\",\n" +
                "  \"observation\": {\n" +
                "    \"unit\": \"DegreesCelcius\",\n" +
                "    \"observedAt\": \"2020-01-16T14:38:19.951845\",\n" +
                "    \"name\": \"tfmtag-example\",\n" +
                "    \"description\": \"Rom 1013, del1, plan U1, Blokk1 \",\n" +
                "    \"source\": {\n" +
                "      \"deviceId\": \"TODO\",\n" +
                "      \"objectId\": \"AnalogInput 3000\"\n" +
                "    },\n" +
                "    \"value\": 22.170061\n" +
                "  }\n" +
                "}";
        DeviceId deviceId = BacnetJsonDeviceIdParser.parse(bacnetJson);
        assertNotNull(deviceId);
        assertEquals("tfmtag-example", deviceId.getTfmTag());

    }
}