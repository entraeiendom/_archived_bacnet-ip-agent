package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.Octet;
import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.ObjectType;
import no.entra.bacnet.json.objects.PropertyIdentifier;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

public class SubscribeCovCommandBuilder {
    private static final Logger log = getLogger(SubscribeCovCommandBuilder.class);
    private double increment = 1.0;
    private ArrayList<ObjectId> subscribeToSensorIds = new ArrayList<>();
    private PropertyIdentifier propertyIdentifier;
    private InetAddress sendToAddress;
    private Boolean confirmedNotifications = null;
    private Exception error = null;
    private Octet invokeId = null; // Identify multiple messages/segments for the same request.
    private Octet subscriptionId = null; // Identify mulitple processes listening on bacnet notifications on a single client.
    private Integer lifetimeSeconds = null;

    public SubscribeCovCommandBuilder(InetAddress sendToAddress) {
        this.sendToAddress = sendToAddress;
    }

    public SubscribeCovCommandBuilder(InetAddress sendToAddress, ObjectId sensorId) {
        this(sendToAddress);
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

    /**
     * Number of seconds a subscription may run on the server.
     * When this parameter is set Bacnet spec require the server to send ConfirmedCOVNotification. And the client must
     * reply with an ack on every Notification.
     * @param seconds The Bacnet Server/Device must support at least 28800 seconds (8 hours)
     * @return this builder.
     */
    public SubscribeCovCommandBuilder withLifetime(int seconds) {
        withConfirmedNotifications(true); //required by the spec.
        this.lifetimeSeconds = seconds;
        return this;
    }

    private ArrayList<ObjectId> mapList(ObjectId[] objectIds) {
        ArrayList<ObjectId> objectIdList = new ArrayList<>();
        objectIdList.addAll(Arrays.asList(objectIds));
        return objectIdList;
    }

    public SubscribeCovCommand build() throws IllegalStateException {
        if (lifetimeSeconds != null && isUnconfirmed()) {
            throw new IllegalStateException("When Lifetime is set. The client must request ConfirmedSubscribeCovCommand or" +
                    "ConfrimedMultipleSubscribeCovCommand.");
        }
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

    boolean isUnconfirmed() {
        return confirmedNotifications == null || confirmedNotifications == false;
    }

    SubscribeCovCommand buildCOVSingleSensor() {
        SubscribeCovCommand covCommand = null;
        ObjectId sensorId = subscribeToSensorIds.get(0);
        if (confirmedNotifications) {
            try {
                covCommand = new ConfirmedSubscribeCovCommand(sendToAddress, sensorId);
                covCommand.setLifetimeSeconds(lifetimeSeconds);
            } catch (IOException e) {
                log.trace("Failed to build ConfirmedSubscribeCovCommand for sendToAddress: {}, and sensorId {}. Reason:",
                        sendToAddress, sensorId, e.getMessage());
                this.error = e;
            }
        } else {
            try {
                covCommand = new UnConfirmedSubscribeCovCommand(sendToAddress, sensorId);
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

    public static void main(String[] args) throws UnknownHostException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Missing address for where to send the subscribeCovCommand. Please provide" +
                    "on command line.");
        }
        String destination = args[0];
        InetAddress sendToAddress = SubscribeCovCommand.inetAddressFromString(destination);
        ObjectId analogValue1 = new ObjectId(ObjectType.AnalogValue, "1");
        int oneDay = 24 * 60 * 60;
        int minutes5 = 5 * 60;
        SubscribeCovCommand covCommand = new SubscribeCovCommandBuilder(sendToAddress, analogValue1)
                .withConfirmedNotifications(false)
                .build();
        try {
            covCommand.sendSubscribeCov();
            Thread.sleep(10000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            covCommand.disconnect();
        }

    }

}
