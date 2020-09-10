package no.entra.bacnet.agent.mqtt;

import no.entra.bacnet.agent.devices.DeviceId;
import no.entra.bacnet.rec.RealEstateCore;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.InetAddress;
import java.util.Optional;

public class PahoMqttClient implements no.entra.bacnet.agent.mqtt.MqttClient {
    @Override
    public void publishRealEstateCore(RealEstateCore message, DeviceId recDeviceId, Optional<InetAddress> sourceAddress) {

    }

    @Override
    public void publishUnknownHexString(String hexString) {

    }

    public static void main(String[] args) {
        String topic        = "bacnet-ip-agent";
        String content      = "Message from MqttPublishSample";
        int qos             = 2;
        String broker       = "tcp://mqtt.eclipse.org:1883";
        String clientId     = "bacnet-ip-agent";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
}
