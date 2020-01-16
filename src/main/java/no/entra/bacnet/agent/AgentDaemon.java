package no.entra.bacnet.agent;

import no.entra.bacnet.agent.devices.DeviceIdRepository;
import no.entra.bacnet.agent.devices.DeviceIdService;
import no.entra.bacnet.agent.devices.InMemoryDeviceIdRepository;
import no.entra.bacnet.agent.mqtt.AzureIoTMqttClient;
import no.entra.bacnet.agent.mqtt.MqttClient;
import no.entra.bacnet.agent.observer.BacnetObserver;
import no.entra.bacnet.agent.observer.BlockingRecordAndForwardObserver;
import no.entra.bacnet.agent.recording.BacnetHexStringRecorder;
import no.entra.bacnet.agent.recording.FileBacnetHexStringRecorder;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.URISyntaxException;

import static no.entra.bacnet.agent.mqtt.AzureIoTMqttClient.DEVICE_CONNECTION_STRING;
import static no.entra.bacnet.agent.utils.PropertyReader.findProperty;
import static no.entra.bacnet.agent.utils.PropertyReader.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

public class AgentDaemon {
    private static final Logger log = getLogger(AgentDaemon.class);

    public static void main(String[] args) {
        try {
            String path = "bacnet-hexstring-recording.log";
            File recordingFile = new File(path);
            BacnetHexStringRecorder hexStringRecorder = new FileBacnetHexStringRecorder(recordingFile);
            boolean connectToMqtt = true;
            MqttClient mqttClient = null;
            if (connectToMqtt) {
                String deviceConnectionString = findConnectionString(args);
                mqttClient = new AzureIoTMqttClient(deviceConnectionString);
            }
            DeviceIdRepository deviceIdRepository = new InMemoryDeviceIdRepository();
            DeviceIdService deviceIdService = new DeviceIdService(deviceIdRepository);
            BacnetObserver bacnetObserver = new BlockingRecordAndForwardObserver(hexStringRecorder, mqttClient, deviceIdService);
            UdpServer udpServer = new UdpServer(bacnetObserver);
            udpServer.setListening(true);
            udpServer.setRecording(true);
            udpServer.start();
        } catch (SocketException e) {
            log.error("Failed to run udpServer.", e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static String findConnectionString(String[] args) {
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
        return deviceConnectionString;
    }
}
