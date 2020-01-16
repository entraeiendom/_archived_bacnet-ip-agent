package no.entra.bacnet.agent.mqtt;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.Message;
import no.entra.bacnet.agent.devices.DeviceId;
import no.entra.bacnet.agent.mqtt.azureiot.Observation;
import no.entra.bacnet.agent.mqtt.azureiot.SendReceive;
import no.entra.bacnet.rec.ConfigurationRequest;
import no.entra.bacnet.rec.RealEstateCore;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.Optional;

import static no.entra.bacnet.agent.mqtt.azureiot.SendReceive.D2C_MESSAGE_TIMEOUT;
import static no.entra.bacnet.agent.utils.PropertyReader.findProperty;
import static org.slf4j.LoggerFactory.getLogger;

public class AzureIoTMqttClient implements MqttClient {
    private static final Logger log = getLogger(AzureIoTMqttClient.class);

    public static final String DEVICE_CONNECTION_STRING = "DEVICE_CONNECTION_STRING";
    public static final String REAL_ESTATE_CORE = "REAL_ESTATE_CORE";
    public static final String MESSAGE_FROM = "MESSAGE_FROM";
    public static final String MESSAGE_TYPE = "MESSAGE_TYPE";
    public static final String UNKNOWN_HEX_STRING = "UNKNOWN_HEX_STRING";
    boolean isConnected = false;

    private String deviceConnectionString = null;
    private String deviceId = null;
    private DeviceClient client;

    public AzureIoTMqttClient(String deviceConnectionString) throws URISyntaxException, IOException {
        this.deviceConnectionString = deviceConnectionString;
        connect();
    }

    /**
     * Used for testing
     */
    protected AzureIoTMqttClient() {
        log.warn("Creating AzureIoTMqttClient for offline testing only.");
    }

    @Override
    public void publishRealEstateCore(RealEstateCore recMessage, DeviceId recDeviceId, Optional<InetAddress> senderAddress) {
        if (recMessage != null) {
            Message msg = buildMqttMessage(recMessage, recDeviceId, senderAddress);
            sendMessage(msg);
        }

    }

    public Message buildMqttMessage(RealEstateCore recMessage, DeviceId recDeviceId, Optional<InetAddress> senderAddress) {
        String msgStr = "{ \n";
        if (recDeviceId != null) {
            msgStr += "\"deviceId\": \"" + recDeviceId.getId() + "\",\n";
        }
        if (recMessage != null) {
            if (recMessage instanceof ConfigurationRequest) {
                msgStr += "\"configurations\": [" + recMessage.toJson() + "]\n";
            } else if (recMessage instanceof Observation) {
                msgStr += "\"observations\": [" + recMessage.toJson() + "]\n";
            }
        }
        msgStr += "}";
        Message msg = new Message(msgStr);
        msg.setContentTypeFinal("application/json");
        if (senderAddress.isPresent()) {
            msg.setProperty(MESSAGE_FROM, senderAddress.get().toString());
        }
        msg.setProperty(MESSAGE_TYPE, REAL_ESTATE_CORE);
        msg.setMessageId(java.util.UUID.randomUUID().toString());
        msg.setExpiryTime(D2C_MESSAGE_TIMEOUT);
        return msg;
    }

    @Override
    public void publishUnknownHexString(String hexString) {
        Message msg = new Message(hexString);
        msg.setContentTypeFinal("text/plain;charset=UTF-8");
        msg.setProperty(MESSAGE_TYPE, UNKNOWN_HEX_STRING);
        msg.setMessageId(java.util.UUID.randomUUID().toString());
        msg.setExpiryTime(D2C_MESSAGE_TIMEOUT);
        sendMessage(msg);

    }

    void sendMessage(Message azureMessage) {

        try {
            SendReceive.EventCallback eventCallback = new SendReceive.EventCallback();
            client.sendEventAsync(azureMessage, eventCallback, azureMessage);
            log.debug("Message is sent: {}", azureMessage.getMessageId());
        } catch (Exception e) {
            log.info("Failed to send azureMessage: {}", azureMessage);
            e.printStackTrace(); // Trace the exception
        }
    }
    void connect() throws URISyntaxException, IOException {
        log.debug("Successfully read input parameters.");
        IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;
        log.debug("Using communication protocol %s.\n", protocol.name());

        client = new DeviceClient(deviceConnectionString, protocol);

        log.debug("Successfully created an IoT Hub client.");

        SendReceive.MessageCallbackMqtt callback = new SendReceive.MessageCallbackMqtt();
        SendReceive.Counter counter = new SendReceive.Counter(0);
        client.setMessageCallback(callback, counter);

        log.debug("Successfully set message callback.");
        // Set your token expiry time limit here
        long time = 2400;
        client.setOption("SetSASTokenExpiryTime", time);
        client.registerConnectionStatusChangeCallback(new SendReceive.IotHubConnectionStatusChangeCallbackLogger(), new Object());
        client.open();
        log.debug("Opened connection to IoT Hub.");
        log.debug("Beginning to receive messages...");
        log.debug("Sending the following event messages: ");
        log.debug("Updated token expiry time to " + time);
        deviceId = client.getConfig().getDeviceId();


    }

    public static void main(String[] args) {


        log.debug("Starting...");

        String deviceConnectionString = findProperty(DEVICE_CONNECTION_STRING);
        if (deviceConnectionString == null) {
            if (args.length > 0) {
                deviceConnectionString = args[0];
            }
        }

        if (isEmpty(deviceConnectionString)) {
            log.error("Missing required property: DEVICE_CONNECTION_STRING, exiting ");
            System.exit(1);
        }


        try {
            AzureIoTMqttClient mqttClient = new AzureIoTMqttClient(deviceConnectionString);
//            String hexString = "810b00190120ffff00ff1000c4020004d22205c4910022036100000000000";
//            mqttClient.publishUnknownHexString(hexString);

            log.debug("Wait for " + D2C_MESSAGE_TIMEOUT / 1000 + " second(s) for response from the IoT Hub...");

            // Wait for IoT Hub to respond.
            try {
                Thread.sleep(D2C_MESSAGE_TIMEOUT);
            } catch (InterruptedException e) {
                log.debug("timeout interupted {}", e.getMessage());
            }

            log.info("In receive mode. Waiting for receiving C2D messages. Press ENTER to close");
//            Scanner scanner = new Scanner(System.in);
//            scanner.nextLine();

            // close the connection
            log.debug("Closing");
            mqttClient.disconnect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*









        double temperature = 20 + Math.random() * 10;
        double humidity = 30 + Math.random() * 20;
        RecMessage recMessage = new RecMessage(deviceId);
        Observation temperatureObservation = new TemperatureObservation("temperatureSensor1", temperature);
        recMessage.addObservation(temperatureObservation);
        String messageId = UUID.randomUUID().toString();
        IoTEdgeMessage ioTEdgeMessage = new IoTEdgeMessage(deviceId, messageId, recMessage);

        String msgStr = gson.toJson(ioTEdgeMessage); //"{\"deviceId\":\"" + deviceId + "\",\"messageId\":" + messageId + ",\"temperature\":" + temperature + ",\"humidity\":" + humidity + "}";

        try {
            Message msg = new Message(msgStr);
            msg.setContentTypeFinal("application/json");
            msg.setProperty("temperatureAlert", temperature > 28 ? "true" : "false");
            msg.setMessageId(java.util.UUID.randomUUID().toString());
            msg.setExpiryTime(D2C_MESSAGE_TIMEOUT);
            log.debug(msgStr);
            SendReceive.EventCallback eventCallback = new SendReceive.EventCallback();
            client.sendEventAsync(msg, eventCallback, msg);
        } catch (Exception e) {
            e.printStackTrace(); // Trace the exception
        }


        log.debug("Wait for " + D2C_MESSAGE_TIMEOUT / 1000 + " second(s) for response from the IoT Hub...");

        // Wait for IoT Hub to respond.
        try {
            Thread.sleep(D2C_MESSAGE_TIMEOUT);
        } catch (InterruptedException e) {
            log.debug("timeout interupted {}", e.getMessage());
        }

        log.info("In receive mode. Waiting for receiving C2D messages. Press ENTER to close");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        // close the connection
        log.debug("Closing");
        client.closeNow();

        if (!failedMessageListOnClose.isEmpty()) {
            log.debug("List of messages that were cancelled on close:" + failedMessageListOnClose.toString());
        }

        log.debug("Shutting down...");
        */
    }

    private void disconnect() {
        if (client != null) {
            try {
                client.closeNow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
