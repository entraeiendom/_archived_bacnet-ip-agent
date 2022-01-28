package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.json.bvlc.BvlcFunction;
import no.entra.bacnet.objects.ObjectId;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static no.entra.bacnet.internal.objects.ObjectIdMapper.toHexString;
import static no.entra.bacnet.json.apdu.SDContextTag.*;
import static no.entra.bacnet.json.utils.HexUtils.intToHexString;
import static org.slf4j.LoggerFactory.getLogger;

/*
Subscribe to a single parameter for Change of Value(COV)
 */
@Deprecated //use bacnet-sdk commands
public class ConfirmedSubscribeCovCommand extends SubscribeCovCommand {
    private static final Logger log = getLogger(ConfirmedSubscribeCovCommand.class);

    public ConfirmedSubscribeCovCommand(InetAddress sendToAddress,int subscriptionId, ObjectId subscribeToSensorId) throws IOException {
        super(sendToAddress, subscriptionId, subscribeToSensorId);
    }

    public ConfirmedSubscribeCovCommand(DatagramSocket socket, InetAddress sendToAddress, int subscriptionId, ObjectId subscribeToSensorId) throws IOException {
        super(socket, sendToAddress, subscriptionId, subscribeToSensorId);
    }

    @Override
    protected String buildHexString() {
        ObjectId deviceSensorId = getSubscribeToSensorIds().get(0);
        return buildConfirmedCovSingleRequest(deviceSensorId);
    }

    /**
     * Create HexString for a Confirmed COV Request to local net, and a single sensor.
     * @return hexString with bvlc, npdu and apdu
     * @param deviceSensorId also known as the supported property.
     */
    protected String buildConfirmedCovSingleRequest(ObjectId deviceSensorId) {
        String hexString;
        String objectIdHex = toHexString(deviceSensorId);
        String confirmEveryNotification = "01";
        String lifetimeHex = buildLifetimeHex();
        String lifetimeParameterHex = TAG3LENGTH1 + lifetimeHex;
        if (lifetimeHex.length() > 2) {
            lifetimeParameterHex = TAG3LENGTH4 + lifetimeHex;
        }
        String apdu = "00020f05"+ TAG0LENGTH1 +"12" + TAG1LENGTH4 + objectIdHex + TAG2LENGTH1 + confirmEveryNotification + lifetimeParameterHex;
        /*
        00 = PDUType = 0
        02 = Max APDU size = 206
        0f = invoke id = 15
        05 = Service Choice 5 - SubscribeCOV-Request
        09 = SD Context Tag 0, Subscriber Process Identifier, Length = 1
        12 = 18 integer
        1c = SD context Tag 1, Monitored Object Identifier, Length = 4
        00000000 = Analog Input, Instance 0
        29 = SD context Tag 2, Issue Confirmed Notification, Length = 1
        01 = True, 00 = false
        39 = SD context Tag 3, Lifetime, Length = 1
        00 = 0 integer == indefinite
         */
        String npdu = "0120ffff00ff";
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

    protected String buildLifetimeHex() {
        String lifetimeHex = LIFETIME_INDEFINITE;

        if (getLifetimeSeconds() != null) {
            int lifetimeSeconds = getLifetimeSeconds();
            if (lifetimeSeconds <= 255) {
                lifetimeHex = intToHexString(lifetimeSeconds, 2);
            } else {
                lifetimeHex = intToHexString(lifetimeSeconds, 8);
            }
        }
        return lifetimeHex;
    }
}
