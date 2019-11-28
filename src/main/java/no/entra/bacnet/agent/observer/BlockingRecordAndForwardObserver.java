package no.entra.bacnet.agent.observer;

import no.entra.bacnet.agent.mqtt.MqttClient;
import no.entra.bacnet.agent.parser.HexStringParser;
import no.entra.bacnet.agent.recording.BacnetHexStringRecorder;
import no.entra.bacnet.json.BacNetParser;
import org.slf4j.Logger;

import static no.entra.bacnet.agent.parser.HexStringParser.hasValue;
import static org.slf4j.LoggerFactory.getLogger;

public class BlockingRecordAndForwardObserver implements BacnetObserver {
    private static final Logger log = getLogger(BlockingRecordAndForwardObserver.class);
    private boolean recording = false;
    private final BacnetHexStringRecorder hexStringRecorder;
    private boolean publishToMqtt = false;
    private MqttClient mqttClient;

    public BlockingRecordAndForwardObserver(BacnetHexStringRecorder hexStringRecorder) {
        this.hexStringRecorder = hexStringRecorder;
        if (hexStringRecorder != null) {
            recording = true;
        }
    }

    public BlockingRecordAndForwardObserver(BacnetHexStringRecorder hexStringRecorder, MqttClient mqttClient) {
        this(hexStringRecorder);
        this.mqttClient = mqttClient;
        if (mqttClient != null) {
            publishToMqtt = true;
        }
    }

    @Override
    public void bacnetHexStringReceived(String hexString) {
        if(recording) {
            hexStringRecorder.persist(hexString);
        }
        if (publishToMqtt) {
            String apduHexString = HexStringParser.findApduHexString(hexString);
            try {
                //TODO fix BacNetParser in constructor
                if (hasValue(apduHexString)) {
                    String json = new BacNetParser().jasonFromApdu(apduHexString);
                    log.debug("Apdu {}\n{}", hexString, json);
                } else {
                    //#2 TODO write unknown hexString to mqtt topic
                    log.debug("No Apdu found for: {}", hexString);
                    mqttClient.publishUnknownHexString(hexString);
                }
            } catch (Exception e) {
                log.debug("Failed to build json from {}. Reason: {}", apduHexString, e.getMessage());
            }
        }
    }
}
