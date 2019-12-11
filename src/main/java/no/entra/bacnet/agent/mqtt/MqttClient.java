package no.entra.bacnet.agent.mqtt;

import no.entra.bacnet.rec.RealEstateCore;

public interface MqttClient {

    void publishRealEstateCore(RealEstateCore message);
    void publishUnknownHexString(String hexString);
}
