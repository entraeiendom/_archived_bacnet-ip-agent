package no.entra.bacnet.agent.observer;

import no.entra.bacnet.agent.devices.DeviceId;
import no.entra.bacnet.agent.devices.DeviceIdRepository;
import no.entra.bacnet.agent.devices.DeviceIdService;
import no.entra.bacnet.agent.mqtt.MqttClient;
import no.entra.bacnet.agent.recording.BacnetHexStringRecorder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BlockingRecordAndForwardObserverTest {

    private BacnetHexStringRecorder hexStringRecorder;
    private MqttClient mqttClient;
    private DeviceIdRepository deviceIdRepository;
    private DeviceIdService deviceIdService;
    private BlockingRecordAndForwardObserver observer;

    @Before
    public void setUp() throws Exception {
        hexStringRecorder = mock(BacnetHexStringRecorder.class);
        mqttClient = mock(MqttClient.class);
        deviceIdRepository = mock(DeviceIdRepository.class);
        deviceIdService = mock(DeviceIdService.class);
        observer = new BlockingRecordAndForwardObserver(hexStringRecorder, mqttClient, deviceIdService );
    }

    @Test
    public void findDeviceId() {
        String tfmTag = "tfm1234";
        String bacnetJson = "{\n" +
                "  \"configurationRequest\": {\n" +
                "    \"observedAt\": \"2020-01-13T14:25:36.433205\",\n" +
                "    \"id\": \"TODO\",\n" +
                "    \"source\": \"0961\",\n" +
                "    \"properties\": {\n" +
                "      \"Request\": \"IHave\",\n" +
                "      \"NotificationClass\": \"0\",\n" +
                "      \"Device\": \"11\",\n" +
                "      \"ObjectName\": \"" + tfmTag + "\"\n" +
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
        DeviceId queryId = new DeviceId();
        queryId.setTfmTag(tfmTag);
        queryId.setGatewayDeviceId(11);
        queryId.setGatewayInstanceNumber(2401);
        String id = "id1234";
        DeviceId persistedId = new DeviceId(id);
        persistedId.setTfmTag(tfmTag);
        queryId.setGatewayDeviceId(11);
        queryId.setGatewayInstanceNumber(2401);
        List<DeviceId> idList = new ArrayList<>();
        idList.add(persistedId);
        when(deviceIdService.findMatching(eq(queryId))).thenReturn(idList);
        DeviceId foundId = observer.findDeviceId(bacnetJson);
        assertNotNull(foundId);
        assertEquals(id, foundId.getId());
        assertEquals(tfmTag, foundId.getTfmTag());

    }

    @Test
    public void findDeviceIdNewDevice() {
        DeviceId queryId = new DeviceId();
        String tfmTag = "tfm1234";
        queryId.setTfmTag(tfmTag);
        List<DeviceId> emptyList = new ArrayList<>();
        when(deviceIdService.findMatching(eq(queryId))).thenReturn(emptyList);
        DeviceId persistedId = new DeviceId("id1234");
        persistedId.setTfmTag(tfmTag);
        when(deviceIdService.createDeviceId(eq(2002),eq("127.0.0.1"), eq(tfmTag))).thenReturn(persistedId);
    }
}