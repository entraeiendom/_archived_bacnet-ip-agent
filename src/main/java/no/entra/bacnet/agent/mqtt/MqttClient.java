package no.entra.bacnet.agent.mqtt;

import no.entra.bacnet.rec.RealEstateCore;

import java.net.InetAddress;
import java.util.Optional;

public interface MqttClient {

    void publishRealEstateCore(RealEstateCore message, Optional<InetAddress> sourceAddress);
    void publishUnknownHexString(String hexString);
}
