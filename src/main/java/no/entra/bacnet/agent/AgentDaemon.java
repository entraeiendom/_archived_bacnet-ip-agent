package no.entra.bacnet.agent;

import no.entra.bacnet.agent.devices.DeviceIdRepository;
import no.entra.bacnet.agent.devices.DeviceIdService;
import no.entra.bacnet.agent.devices.InMemoryDeviceIdRepository;
import no.entra.bacnet.agent.importer.DeviceImporter;
import no.entra.bacnet.agent.mqtt.MqttClient;
import no.entra.bacnet.agent.mqtt.PahoMqttClient;
import no.entra.bacnet.agent.observer.BacnetObserver;
import no.entra.bacnet.agent.observer.BlockingRecordAndForwardObserver;
import no.entra.bacnet.agent.recording.BacnetHexStringRecorder;
import no.entra.bacnet.agent.recording.FileBacnetHexStringRecorder;
import org.slf4j.Logger;

import java.io.File;

import static no.entra.bacnet.agent.mqtt.AzureIoTMqttClient.DEVICE_CONNECTION_STRING;
import static no.entra.bacnet.agent.mqtt.PahoMqttClient.*;
import static no.entra.bacnet.agent.utils.PropertyReader.findProperty;
import static no.entra.bacnet.agent.utils.PropertyReader.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

public class AgentDaemon {
    private static final Logger log = getLogger(AgentDaemon.class);
    private static final int WAIT_DEVICES_TO_REPORT_IN_SEC = 10;

    public static void main(String[] args) {
        try {
            String path = "bacnet-hexstring-recording.log";
            File recordingFile = new File(path);
            BacnetHexStringRecorder hexStringRecorder = new FileBacnetHexStringRecorder(recordingFile);
            boolean connectToMqtt = true;
            boolean findDeviceInfo = true;
            MqttClient mqttClient = null;
            if (connectToMqtt) {
//                String deviceConnectionString = findConnectionString(args);
//                mqttClient = new AzureIoTMqttClient(deviceConnectionString);
                String brokerUrl = findProperty(MQTT_BROKER_URL);
                String username = findProperty(MQTT_USERNAME);
                String password = findProperty(MQTT_PASSWORD);
                String topic = findProperty(MQTT_TOPIC);
                String clientId = findProperty(MQTT_CLIENT_ID);
                mqttClient = new PahoMqttClient(brokerUrl,username,password,topic, clientId);
            }
            DeviceIdRepository deviceIdRepository = new InMemoryDeviceIdRepository();
            DeviceIdService deviceIdService = new DeviceIdService(deviceIdRepository);
            BacnetObserver bacnetObserver = new BlockingRecordAndForwardObserver(hexStringRecorder, mqttClient, deviceIdService);
            UdpServer udpServer = new UdpServer(bacnetObserver);
            udpServer.setListening(true);
            udpServer.setRecording(true);
            udpServer.start();

            if (findDeviceInfo) {
                DeviceImporter deviceImporter = new DeviceImporter(deviceIdService);
                try {
                    deviceImporter.findDevices();
                    log.info("Running...find devices.");
                    log.trace("Wait {} seconds for devices to report in.", WAIT_DEVICES_TO_REPORT_IN_SEC);
                    Thread.sleep(WAIT_DEVICES_TO_REPORT_IN_SEC * 1000);
                    log.info("Find Sensors and Properties each Device Supports. ");
                    deviceImporter.findSensorsAndPropertiesTheDevicesSupports();
                    log.info("Find information about each Sensor and Property configuration. ");
                    deviceImporter.findSensorAndPropertiesConfiguration();
                    log.info("Find Present Value for each sensor.");
                    deviceImporter.findPresentValueForSensors();
                } catch (Exception e) {
                    log.info("Exception trying to run scheduled imports of trendIds. Reason: {}", e.getMessage(), e);
                }
                //TODO re-enable schedler
//                ScheduledDeviceImporter scheduledDeviceImporter = new ScheduledDeviceImporter(oneTimeDeviceImporter);
//                scheduledDeviceImporter.startScheduledImport();
            }
        } catch (Exception e) {
            log.error("Failed to run udpServer.", e);
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
