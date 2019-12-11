package no.entra.bacnet.agent.mqtt;

import no.entra.bacnet.agent.RealEstateCoreMessage;

public interface MqttClient {

    void publishRealEstateCore(RealEstateCoreMessage message);
    void publishUnknownHexString(String hexString);
}
