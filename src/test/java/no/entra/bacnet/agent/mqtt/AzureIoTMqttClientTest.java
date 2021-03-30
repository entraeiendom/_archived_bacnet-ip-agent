package no.entra.bacnet.agent.mqtt;

import com.microsoft.azure.sdk.iot.device.Message;
import no.entra.bacnet.agent.devices.DeviceId;
import no.entra.bacnet.rec.ConfigurationRequest;
import no.entra.bacnet.utils.DateTimeHelper;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;

public class AzureIoTMqttClientTest {

    private Instant observationTime;
    private AzureIoTMqttClient mqttClient;
    private String expectedJson = "{\n" +
            "  \"deviceId\": \"id1234\",\n" +
            "  \"configurations\": [{\n" +
            "    \"sender\": \"sendt from\",\n" +
            "    \"recipient\": \"recip\",\n" +
            "    \"observationTime\": \"2019-12-09T20:57:17.776468\",\n" +
            "    \"id\": \"123\",\n" +
            "    \"type\": \"IHave\",\n" +
            "    \"properties\": {\n" +
            "      \"ObjectName\": \"TFM434\"\n" +
            "    }\n" +
            "  }]\n" +
            "}";

    @Before
    public void setUp() throws Exception {
        String localDateTime = "2019-12-09T20:57:17.776Z";
        observationTime = DateTimeHelper.fromIso8601Json(localDateTime);
        mqttClient = new AzureIoTMqttClient();
    }

    @Test
    public void buildMqttMessage() {
        ConfigurationRequest recMessage = new ConfigurationRequest();
        recMessage.setObservationTime(observationTime);
        recMessage.setId("123");
        recMessage.setRecipient("recip");
        recMessage.setSender("sendt from");
        recMessage.setType("IHave");
        recMessage.addProperty("ObjectName", "TFM434");
        DeviceId recDeviceId = new DeviceId("id1234");
        recDeviceId.setTfmTag("TFM767");
        Message mqttMessage = mqttClient.buildMqttMessage(recMessage, recDeviceId, Optional.empty());
        assertNotNull(mqttMessage);
        byte[] bodyBytes = mqttMessage.getBytes();
        String bodyJson = new String(bodyBytes);
        assertNotNull(bodyJson);
        JSONAssert.assertEquals(expectedJson, bodyJson, true);
    }
}