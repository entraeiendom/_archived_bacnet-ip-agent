package no.entra.bacnet.agent.mqtt;

import org.slf4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.slf4j.LoggerFactory.getLogger;

public class AzureIoTMqttClientManualTest {
    private static final Logger log = getLogger(AzureIoTMqttClientManualTest.class);


    public static void main(String[] args) throws InterruptedException {
        String deviceConnectionString = args[0];
        try {
            AzureIoTMqttClient client = new AzureIoTMqttClient(deviceConnectionString);
        } catch (URISyntaxException e) {
            log.info("Wrong URI using {}", deviceConnectionString, e);
        } catch (IOException e) {
            log.debug("Failed to connect to AzureIoT using {}", deviceConnectionString, e);
        }
        Thread.sleep(10000);
    }

}