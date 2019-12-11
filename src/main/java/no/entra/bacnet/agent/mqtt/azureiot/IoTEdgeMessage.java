package no.entra.bacnet.agent.mqtt.azureiot;

public class IoTEdgeMessage {
    private final String deviceId;
    private final String messageId;
    private final RecMessage recMessage;

    public IoTEdgeMessage(String deviceId, String messageId, RecMessage recMessage) {
        this.deviceId = deviceId;
        this.messageId = messageId;
        this.recMessage = recMessage;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getMessageId() {
        return messageId;
    }

    public RecMessage getRecMessage() {
        return recMessage;
    }

    @Override
    public String toString() {
        return "IoTEdgeMessage{" +
                "deviceId='" + deviceId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", recMessage=" + recMessage.toString() +
                '}';
    }
}
