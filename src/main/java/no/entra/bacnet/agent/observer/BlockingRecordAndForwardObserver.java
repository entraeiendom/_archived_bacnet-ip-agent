package no.entra.bacnet.agent.observer;

import no.entra.bacnet.agent.mqtt.MqttClient;
import no.entra.bacnet.agent.recording.BacnetHexStringRecorder;
import no.entra.bacnet.json.Bacnet2Json;
import no.entra.bacnet.rec.Bacnet2Rec;
import no.entra.bacnet.rec.RealEstateCore;
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
            try {
                if (hasValue(hexString)) {
                    String bacnetJson = Bacnet2Json.hexStringToJson(hexString);
                    log.trace("BacnetJson {}\n{}", hexString, bacnetJson);
                    if (bacnetJson != null) {
                        RealEstateCore message = null;
                        try {
                            message = Bacnet2Rec.bacnetToRec(bacnetJson);
                            if (message != null) {
                                mqttClient.publishRealEstateCore(message);
                                log.info("Message is published from bacnetJson: {}", bacnetJson);
                            } else {
                                log.trace("Could not send empty message from bacnetJson: {}", bacnetJson);
                            }
                        } catch (Exception e) {
                            log.trace("Failed to send message to AzureIoT. hexString: {}\nMessage: {},\n reason {}", hexString, message, e.getMessage());
                            //mqttClient.publishUnknownHexString(hexString);
                        }
                    }
                } else {
                    //#2 TODO write unknown hexString to mqtt topic
                    log.debug("No Apdu found for: {}", hexString);
                   // mqttClient.publishUnknownHexString(hexString);
                }
            } catch (Exception e) {
                log.debug("Failed to build json from {}. Reason: {}", hexString, e.getMessage());
            }
        }
    }
}
