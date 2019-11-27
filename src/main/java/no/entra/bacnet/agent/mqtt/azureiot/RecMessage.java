package no.entra.bacnet.agent.mqtt.azureiot;

import java.util.Arrays;
import java.util.List;

public class RecMessage {
    private final String format = "rec3.1";
    private final String deviceId;
    private Observation[] observations;

    public RecMessage(String deviceId) {
        if (deviceId != null && deviceId.startsWith("https://recref.com/device/")) {
            this.deviceId = deviceId;
        } else {
            this.deviceId = "https://recref.com/device/" + deviceId;
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void addObservation(Observation observation) {
        if (observations == null) {
            observations = new Observation[] {observation};
        } else {
            List<Observation> observationList = List.of(observations);
            observationList.add(observation);
            observations = (Observation[]) observationList.toArray();
        }

    }

    public Observation[] getObservations() {
        return observations;
    }

    @Override
    public String toString() {
        return "RecMessage{" +
                "format='" + format + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", observations=" + Arrays.toString(observations) +
                '}';
    }
}
