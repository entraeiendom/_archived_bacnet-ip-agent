package no.entra.bacnet.agent.sensors;

import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.PropertyIdentifier;

import java.util.ArrayList;
import java.util.List;

public class SensorWithProperties {

    private final ObjectId sensorId;
    private List<PropertyIdentifier> propertyIds = new ArrayList<>();

    public SensorWithProperties(ObjectId sensorId) {
        this.sensorId = sensorId;
    }

    public SensorWithProperties(ObjectId sensorId, PropertyIdentifier... propertyId) {
        this.sensorId = sensorId;
        this.propertyIds = propertyIds;
    }

    public void withPropertyId(PropertyIdentifier propertyId) {
        propertyIds.add(propertyId);
    }

    public ObjectId getSensorId() {
        return sensorId;
    }

    public List<PropertyIdentifier> getPropertyIds() {
        return propertyIds;
    }

    @Override
    public String toString() {
        return "SensorWithProperties{" +
                "sensorId=" + sensorId +
                ", propertyIds=" + propertyIds +
                '}';
    }
}
