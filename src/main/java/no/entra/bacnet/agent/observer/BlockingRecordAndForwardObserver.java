package no.entra.bacnet.agent.observer;

import no.entra.bacnet.agent.devices.BacnetJsonDeviceIdParser;
import no.entra.bacnet.agent.devices.DeviceId;
import no.entra.bacnet.agent.devices.DeviceIdService;
import no.entra.bacnet.agent.mqtt.MqttClient;
import no.entra.bacnet.agent.recording.BacnetHexStringRecorder;
import no.entra.bacnet.json.Bacnet2Json;
import no.entra.bacnet.rec.Bacnet2Rec;
import no.entra.bacnet.rec.RealEstateCore;
import org.slf4j.Logger;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import static no.entra.bacnet.agent.parser.HexStringParser.hasValue;
import static org.slf4j.LoggerFactory.getLogger;

public class BlockingRecordAndForwardObserver implements BacnetObserver {
    private static final Logger log = getLogger(BlockingRecordAndForwardObserver.class);
    private boolean recording = false;
    private final BacnetHexStringRecorder hexStringRecorder;
    private boolean publishToMqtt = false;
    private MqttClient mqttClient;
    private DeviceIdService deviceIdService = null;

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

    public BlockingRecordAndForwardObserver(BacnetHexStringRecorder hexStringRecorder, MqttClient mqttClient, DeviceIdService deviceIdService) {
        this(hexStringRecorder, mqttClient);
        this.deviceIdService = deviceIdService;
    }

    @Override
    public void bacnetHexStringReceived(InetAddress sourceAddress, String hexString) {
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
                            DeviceId recDeviceId = findDeviceId(bacnetJson);
                            message = Bacnet2Rec.bacnetToRec(bacnetJson);
                            if (message != null) {
                                message.setSenderAddress(sourceAddress.toString());
                                mqttClient.publishRealEstateCore(message, recDeviceId, Optional.of(sourceAddress));
                                log.info("Message is published from bacnetJson: {}", bacnetJson);
                            } else {
                                log.trace("Could not send empty message from bacnetJson: {}", bacnetJson);
                            }
                        } catch (Exception e) {
                            log.trace("Failed to send message to AzureIoT. hexString: {}\nMessage: {},\n reason {}", hexString, message, e.getMessage());
                            //mqttClient.publishUnknownHexString(hexString);
                            e.printStackTrace();
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

    DeviceId findDeviceId(String bacnetJson) {
        DeviceId deviceId = null;
        if (hasValue(bacnetJson)) {
            deviceId = BacnetJsonDeviceIdParser.parse(bacnetJson);
            if (deviceId != null) {
                List<DeviceId> matchingIds = deviceIdService.findMatching(deviceId);
                if (matchingIds == null || matchingIds.isEmpty()) {
                    Integer instanceNumber = deviceId.getInstanceNumber();
                    String ipAddress = deviceId.getIpAddress();
                    String tfmTag = deviceId.getTfmTag();
                    log.trace("DeviceIdService: {}", deviceIdService);
                    DeviceId createdId = deviceIdService.createDeviceId(instanceNumber, ipAddress, tfmTag);
                    if (createdId == null) {
                        log.trace("Could not create new id for deviceId based on {}", deviceId);
                    } else {
                        deviceId.setId(createdId.getId());
                    }
                } else if (matchingIds.size() == 1) {
                    DeviceId matchedId = matchingIds.get(0);
                    String id = matchedId.getId();
                    deviceId.setId(id);
                } else {
                    //TODO #23
                    log.info("More than one matching id. Code need to be extended to handle this situation. Matched on " +
                            "deviceId: {}. Found: {} matcing ids.", deviceId, matchingIds.size());
                }
            }
        }
        return deviceId;
    }
}
