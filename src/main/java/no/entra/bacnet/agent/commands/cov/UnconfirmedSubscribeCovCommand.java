package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.json.bvlc.BvlcFunction;
import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.ObjectIdMapper;
import no.entra.bacnet.json.objects.ObjectType;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

import static no.entra.bacnet.json.apdu.SDContextTag.*;
import static org.slf4j.LoggerFactory.getLogger;

/*
Subscribe to a single parameter for Change of Value(COV)
 */
public class UnconfirmedSubscribeCovCommand extends SubscribeCovCommand {
    private static final Logger log = getLogger(UnconfirmedSubscribeCovCommand.class);
    public static final String UNCONFIRMED = "00";

    public UnconfirmedSubscribeCovCommand() throws SocketException {
        super();
    }

    UnconfirmedSubscribeCovCommand(DatagramSocket socket) {
        super(socket);
    }

    protected String buildHexString(ObjectId deviceSensorId) {
        return buildUnConfirmedCovSingleRequest(deviceSensorId);
    }

    /**
     * Create HexString for a Confirmed COV Request to local net, and a single sensor.
     * @return hexString with bvlc, npdu and apdu
     * @param deviceSensorId also known as the supported property.
     */
    protected String buildUnConfirmedCovSingleRequest(ObjectId deviceSensorId) {
        String hexString = null;
        String objectIdHex = ObjectIdMapper.toHexString(deviceSensorId);
        String confirmEveryNotification = UNCONFIRMED;
        String lifetimeHex = "00"; //indefinite
        String apdu = "00020f05"+ TAG0LENGTH1 +"12" + TAG1LENGTH4 + objectIdHex + TAG2LENGTH1 + confirmEveryNotification + TAG3LENGTH1 + lifetimeHex;
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

        hexString = bvlc.toString() + npdu.toString() + apdu;
        log.debug("Hex to send: {}", hexString);
        return hexString;
    }


    public static void main(String[] args) {
        SubscribeCovCommand client = null;

        //Destination may also be fetched as the first program argument.
        String destination = null;
        if (args.length > 0) {
            destination = args[0];
        }
        try {
            client = new UnconfirmedSubscribeCovCommand();
            ObjectId parameterToWatch = new ObjectId(ObjectType.AnalogValue, "1");
            if (destination == null) {
                client.broadcast();
            } else {
                client.local(destination, parameterToWatch);
            }
            Thread.sleep(10000);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            client.disconnect();
        }
    }
}
