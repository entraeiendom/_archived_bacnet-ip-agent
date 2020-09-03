package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.Octet;
import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.PropertyIdentifier;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class SubscribeCovCommandBuilder {
    private static final Logger log = getLogger(SubscribeCovCommandBuilder.class);
    private double increment = 1.0;
    private ArrayList<ObjectId> subscribeToSensorIds = new ArrayList<>();
    private PropertyIdentifier propertyIdentifier;
    private InetAddress sendToAddress;
    private boolean confirmedNotifications = false;
    private Exception error = null;
    private Octet invokeId = null; // Identify multiple messages/segments for the same request.
    private Octet subscriptionId = null; // Identify mulitple processes listening on bacnet notifications on a single client.

    public SubscribeCovCommandBuilder(InetAddress sendToAddress) {
        this.sendToAddress = sendToAddress;
    }

    public SubscribeCovCommandBuilder(InetAddress sendToAddress, ObjectId sensorId) {
        withSensors(sensorId);
    }

    public SubscribeCovCommandBuilder withSendToAddress(InetAddress sendToAddress) {
        this.sendToAddress = sendToAddress;
        return this;
    }

    public SubscribeCovCommandBuilder withObserveProperty(PropertyIdentifier propertyIdentifier) {
        this.propertyIdentifier = propertyIdentifier;
        return this;
    }

    public SubscribeCovCommandBuilder withSensors(ObjectId... sensorIds) {
        ArrayList<ObjectId> sensors = mapList(sensorIds);
        subscribeToSensorIds.addAll(sensors);
        return this;
    }

    public SubscribeCovCommandBuilder withIncrement(double increment) {
        this.increment = increment;
        return this;
    }

    public SubscribeCovCommandBuilder withConfirmedNotifications(boolean confirmedNotifications) {
        this.confirmedNotifications = confirmedNotifications;
        return this;
    }

    public SubscribeCovCommandBuilder withSubscriptionId(Octet subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public SubscribeCovCommandBuilder withInvokeId(Octet invokeId) {
        this.invokeId = invokeId;
        return this;
    }

    private ArrayList<ObjectId> mapList(ObjectId[] objectIds) {
        ArrayList<ObjectId> objectIdList = new ArrayList<>();
        for (ObjectId objectId : objectIds) {
            objectIdList.add(objectId);
        }
        return objectIdList;
    }

    public SubscribeCovCommand build() {
        SubscribeCovCommand covCommand = null;
        if (subscribeToSensorIds.size() > 1) {
            covCommand = buildCOVMultipleSensors();
        } else {
            covCommand = buildCOVSingleSensor();
        }
        if (invokeId != null) {
            covCommand.setInvokeId(invokeId);
        }
        if (subscriptionId != null) {
            covCommand.setSubscriptionId(subscriptionId);
        }
        return covCommand;
    }

    SubscribeCovCommand buildCOVSingleSensor() {
        SubscribeCovCommand covCommand = null;
        ObjectId sensorId = subscribeToSensorIds.get(0);
        if (confirmedNotifications) {
            try {
                covCommand = new ConfirmedSubscribeCovCommand(sendToAddress, sensorId);
            } catch (IOException e) {
                log.trace("Failed to build ConfirmedSubscribeCovCommand for sendToAddress: {}, and sensorId {}. Reason:",
                        sendToAddress, sensorId, e.getMessage());
                this.error = e;
            }
        } else {
            try {
                covCommand = new UnconfirmedSubscribeCovCommand(sendToAddress, sensorId);
            } catch (IOException e) {
                log.trace("Failed to build UnconfirmedSubscribeCovCommand for sendToAddress: {}, and sensorId {}. Reason:",
                        sendToAddress, sensorId, e.getMessage());
                this.error = e;
            }
        }
        return covCommand;
    }

    SubscribeCovCommand buildCOVMultipleSensors() {
        return null;
    }

    public Exception getError() {
        return error;
    }
}
