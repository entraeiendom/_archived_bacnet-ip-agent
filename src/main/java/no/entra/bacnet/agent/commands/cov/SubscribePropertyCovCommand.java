package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.json.objects.PropertyIdentifier;
import no.entra.bacnet.objects.ObjectId;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 */
public class SubscribePropertyCovCommand extends SubscribeCovCommand {
    private static final Double DEFAULT_INCREMENT = null;
    public static boolean DEFAULT_EXPECT_CONFIRMED_NOTIFICATION = true;

    /**
     * Monitor changes to PRESENT_VALUE of a single, or set of sensors.
     * @param sendToAddress - ip address for BacnetDevice (publisher) that must issue ChangeOfValue notifications (COV)
     * @param subscriptionId - the client must create a "subscription id". This id will be used by the publisher when providing notifications,
     *                          when the client is canceling a subscription. The same id must be used when canceling an subscription.
     *                       Named "Subscriber Process Identifier" in Bacnet standard.
     * @param subscribeToSensorIds - identify which of the available sensors to monitor
     *                             Named "Monitored Object Identifier" in Bacnet standard.
     * @throws IOException
     */
    public SubscribePropertyCovCommand(InetAddress sendToAddress, int subscriptionId, ObjectId... subscribeToSensorIds ) throws IOException {
        this(sendToAddress, subscriptionId, PropertyIdentifier.PresentValue, DEFAULT_EXPECT_CONFIRMED_NOTIFICATION,DEFAULT_INCREMENT, subscribeToSensorIds);
    }


    protected SubscribePropertyCovCommand(DatagramSocket socket, InetAddress sendToAddress, int subscriptionId, ObjectId... subscribeToSensorIds) throws IOException {
        super(socket, sendToAddress, subscriptionId, subscribeToSensorIds);
    }

    public SubscribePropertyCovCommand(InetAddress sendToAddress, int subscriptionId, PropertyIdentifier propertyToObserve, boolean expectConfirmedNotifications, Double increment, ObjectId... subscribeToSensorIds) throws IOException {
        super(sendToAddress, subscriptionId, subscribeToSensorIds);
//        this.processIdentifier, presentValue, expectConfirmedNotifications, increment);
    }

    @Override
    protected String buildHexString() {
        return null;
    }
}
