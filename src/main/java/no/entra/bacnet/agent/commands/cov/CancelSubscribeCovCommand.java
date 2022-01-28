package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.json.bvlc.BvlcFunction;
import no.entra.bacnet.objects.ObjectId;
import no.entra.bacnet.objects.ObjectType;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static no.entra.bacnet.internal.objects.ObjectIdMapper.toHexString;
import static no.entra.bacnet.json.apdu.SDContextTag.TAG0LENGTH1;
import static no.entra.bacnet.json.apdu.SDContextTag.TAG1LENGTH4;
import static no.entra.bacnet.json.objects.PduType.ConfirmedRequest;
import static no.entra.bacnet.json.services.ConfirmedServiceChoice.SubscribeCov;
import static org.slf4j.LoggerFactory.getLogger;

/*
When the parameters "lifetime" and "Issue Confirmed Notifications" are missing the server will understand this request
as cancel subscription.
 */
@Deprecated //use bacnet-sdk commands
public class CancelSubscribeCovCommand extends SubscribeCovCommand {
    private static final Logger log = getLogger(CancelSubscribeCovCommand.class);

    private final Integer lifetime = null;
    private Boolean confirmedNotifications = null;

    public CancelSubscribeCovCommand(InetAddress sendToAddress, int subscriptionId, ObjectId sensorObjectId) throws IOException {
        super(sendToAddress, subscriptionId, sensorObjectId);
    }
    public CancelSubscribeCovCommand(DatagramSocket socket, InetAddress sendToAddress, int subscriptionId, ObjectId sensorObjectId) throws IOException {
        super(socket, sendToAddress, subscriptionId, sensorObjectId);
    }

    @Override
    protected String buildHexString() {
        String hexString = null;
        ObjectId deviceSensorId = getSubscribeToSensorIds().get(0);
        String objectIdHex = toHexString(deviceSensorId);

        String pduTypeHex = ConfirmedRequest.getPduTypeChar() + "0";
        String serviceChoiceHex = SubscribeCov.getServiceChoiceHex();
        String invokeIdHex = getInvokeId().toString();
        String maxApduLengthHex = "02"; //TODO need to be able to set this.;
        //When a client have multiple processes subscribing to the server. Use this parameter to route notifications to the
        //corresponding client process. - Not much in use in a Java implementation.
        String subscriberProcessIdentifier = getSubscriptionIdHex().toString();
        String apdu = pduTypeHex + maxApduLengthHex + invokeIdHex + serviceChoiceHex + TAG0LENGTH1 +
                subscriberProcessIdentifier + TAG1LENGTH4 + objectIdHex;
        /*
        00 = PDUType = 0
        02 = Max APDU size = 206
        0f = invoke id = 15 // Identify multiple messages/segments for the same request.
        05 = Service Choice 5 - SubscribeCOV-Request
        09 = SD Context Tag 0, Subscriber Process Identifier, Length = 1
        12 = 18 integer
        1c = SD context Tag 1, Monitored Object Identifier, Length = 4
        00000000 = Analog Input, Instance 0
         */
        String npdu = "0120ffff00ff"; //TODO need to objectify this.
        int numberOfOctets = (apdu.length() + npdu.length() + 8) / 2;
        String messageLength = Integer.toHexString(numberOfOctets);
        if (numberOfOctets <= 255) {
            messageLength = "00" + messageLength;
        }
        String bvlc = "81" + BvlcFunction.OriginalUnicastNpdu.getBvlcFunctionHex() + messageLength;

        hexString = bvlc + npdu + apdu;
        log.debug("Hex to send: {}", hexString);
        return hexString;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException("Missing address for where to send the subscribeCovCommand and the integer of SubscriptionId. Please provide" +
                    "on command line. example: \"192.168.1.100 12\"");
        }
        String destination = args[0];
        int subscriptionId;
        if (args.length > 1) {
            subscriptionId = Integer.valueOf(args[1]);
        } else {
            throw new IllegalArgumentException("Missing subscriptionId. It must be argument 2");
        }
        InetAddress sendToAddress = SubscribeCovCommand.inetAddressFromString(destination);
        ObjectId analogValue1 = new ObjectId(ObjectType.AnalogValue, 1);
        SubscribeCovCommand covCommand = new CancelSubscribeCovCommand(sendToAddress, subscriptionId, analogValue1);
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
