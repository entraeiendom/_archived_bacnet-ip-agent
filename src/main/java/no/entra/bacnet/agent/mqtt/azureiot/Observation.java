package no.entra.bacnet.agent.mqtt.azureiot;

import java.time.Instant;

public abstract class Observation {
    public static final String REC_NS_SENSOR = "https://recref.com/sensor/";
    private final String sensorId;
    private final String quantityKind;
    private final String observationTime;

    public Observation(String sensorId, String quantityKind) {
        this.sensorId = REC_NS_SENSOR + sensorId;
        if (quantityKind != null && quantityKind.startsWith("https://w3id.org/rec/core")) {
            this.quantityKind = quantityKind;
        } else {
            this.quantityKind = "https://recref.com/rec/core/" + quantityKind;
        }
        observationTime = Instant.now().toString();
    }


    /**
     * Used primarilly for testing.
     * @param namespacedSensorId require https://recref.com/sensor/{sensorId}
     * @param namespacedQuantityKind require https://w3id.org/rec/core/{quantityKind}
     * @param observationTime
     */
    protected Observation(String namespacedSensorId, String namespacedQuantityKind, Instant observationTime) {
        this.sensorId = namespacedSensorId;
        this.quantityKind = namespacedQuantityKind;
        this.observationTime = observationTime.toString();
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getQuantityKind() {
        return quantityKind;
    }

    /**
     * Implement this metod to return either String or Nummeric value
     * @return
     */
    abstract Object getValue();

    @Override
    public String toString() {
        return "Observation{" +
                "sensorId='" + sensorId + '\'' +
                ", quantityKind='" + quantityKind + '\'' +
                ", observationTime='" + observationTime + '\'' +
                '}';
    }
}
