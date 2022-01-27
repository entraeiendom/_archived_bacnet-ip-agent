package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.json.bvlc.BvlcFunction;
import no.entra.bacnet.objects.ObjectId;
import no.entra.bacnet.objects.ObjectType;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static no.entra.bacnet.json.apdu.SDContextTag.*;
import static org.slf4j.LoggerFactory.getLogger;

/*
Subscribe to a multiple parameters for Change of Value(COV)
 */
public class UnConfirmedMultipleSubscribeCovCommand extends SubscribeCovCommand {
    private static final Logger log = getLogger(UnConfirmedMultipleSubscribeCovCommand.class);
    public static final String UNCONFIRMED = "00";
    public static final String CONFIRMED = "01";
    public static final String PDTAG0_OPEN = "0e";
    public static final String PDTAG0_CLOSE = "0f";
    public static final String PDTAG1_OPEN = "1e";
    public static final String PDTAG1_CLOSE = "1f";
    public static final String PDTAG4_OPEN = "4e";
    public static final String PDTAG4_CLOSE = "4f";

    public UnConfirmedMultipleSubscribeCovCommand(InetAddress sendToAddress, ObjectId... subscribeToSensorIds) throws IOException {
        super(sendToAddress, subscribeToSensorIds);
    }

    public UnConfirmedMultipleSubscribeCovCommand(DatagramSocket socket, InetAddress sendToAddress, ObjectId... subscribeToSensorIds) throws IOException {
        super(socket, sendToAddress, subscribeToSensorIds);
    }

    @Override
    protected String buildHexString() {
        return buildUnconfirmedMultipeCovRequest();
    }

    /**
     * Create HexString for a Confirmed COV Request with multiple sensors
     * @return hexString with bvlc, npdu and apdu
     */
    protected String buildUnconfirmedMultipeCovRequest() {
        String hexString = null;
        String confirmEveryNotification = CONFIRMED; //UNCONFIRMED;
        String lifetimeHex = "3c"; //60 seconds?
        String maxDelayHex = "05"; //5 seconds?
        String apdu = "00020f1e" + TAG0LENGTH1 +"12" + TAG1LENGTH1 + confirmEveryNotification + TAG2LENGTH1 + lifetimeHex + TAG3LENGTH1 + maxDelayHex;
        String monitorProperty1PresentValueHex = "0e09550f";
        String covIncrementHex = "1c3f800000";
        String timestampedHex = "2901";
        String monitorProperty2RelibilityHex = "0e09670f";
        String notTimestampedHex = "2900";
        String sensorObject1Hex = "0c00800001" + PDTAG1_OPEN + monitorProperty1PresentValueHex + covIncrementHex + timestampedHex + monitorProperty2RelibilityHex + notTimestampedHex + PDTAG1_CLOSE;
        String sensorObject2Hex = "0c00800000" + PDTAG1_OPEN + monitorProperty1PresentValueHex + covIncrementHex + timestampedHex + PDTAG1_CLOSE;
        apdu = apdu + PDTAG4_OPEN + sensorObject1Hex + sensorObject2Hex + PDTAG4_CLOSE;
        /*
        00 = PDUType = 0
        02 = Max APDU size = 206
        0f = invoke id = 15
        1e = Service Choice 30 - SubscribeCOVPropertyMultiple-Request
        09 = SD Context Tag 0, Subscriber Process Identifier, Length = 1
        12 = 18 integer
        19 = SD context Tag 1, Issue Confirmed Notifications, Length = 1
        00 = False
        29 = SD context Tag 2, Lifetime, Length = 1
        3c = 60
        39 = SD context Tag 3, Max Notification Delay, Length = 1
        05 = 5
        The list:
        4e PD Opening Tag 4, List of COV Subscription Specifications
          * Sensor Object number 1
          0c = SD Context Tag 0, Monitored Object, Length = 4
          00800001 = Analog Value, Instance 1
          1e = PD Opening Tag 1, List of COV References
            0e = PD Opening Tag 0, Monitored Property
              09 = SD Context Tag 0, Property Identifier, Length 1
              55 = 85, Present Value
            0f = PD Closing Tag 0
            1c = SD Context Tag 1, COV Increment, Length = 4
            3f800000 = 1.0 - Real value
            29 = SD Context Tag 2, Timestamped, Length = 1
            01 = True
            0e = PD OpeningTag 0, Monitored Property
              09 = SD Context Tag 0, Property Identifier, Length 1
              67 = 103 Reliability
            0f = PD Closing Tag 0
            29 = SD Context Tag 2, Timestamped, Length 1
            00 = False
          1f = PD Closing Tag 1
          * Sensor Object number 2
          0c
          00800000 = Analog Value, Instance 0
          1e = PD Opening Tag 1, List of COV References
            0e = PD Opening Tag 0, Monitored Property
              09 = SD Context Tag 0, Property Identifier, Length 1
              55 = 85, Present Value
            0f = PD Closing Tag 0
            1c = SD Context Tag 1, COV Increment, Length = 4
            3f800000 = 1.0 - Real value
            29 = SD Context Tag 2, Timestamped, Length = 1
            01 = True
          1f = PD Closing Tag 1
        4f PD Closing Tag 4, List of COV Subscription Specifications
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
        UnConfirmedMultipleSubscribeCovCommand covCommand = null;

        //Destination may also be fetched as the first program argument.
        String destination = BROADCAST_IP;
        if (args.length > 0) {
            destination = args[0];
        }
        try {
            ObjectId analogValue1 = new ObjectId(ObjectType.AnalogValue, 1);
            ObjectId analogValue0 = new ObjectId(ObjectType.AnalogValue, 0);
            InetAddress sendToAddress = SubscribeCovCommand.inetAddressFromString(destination);
            covCommand = new UnConfirmedMultipleSubscribeCovCommand(sendToAddress, analogValue1, analogValue0);
            covCommand.execute();
            Thread.sleep(10000);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            covCommand.disconnect();
        }
    }
}
