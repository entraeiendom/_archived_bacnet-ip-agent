package no.entra.bacnet.agent.commands;

import no.entra.bacnet.json.bvlc.BvlcFunction;
import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.ObjectIdMapper;
import no.entra.bacnet.json.objects.ObjectType;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.*;

import static no.entra.bacnet.agent.utils.ByteHexConverter.hexStringToByteArray;
import static no.entra.bacnet.json.apdu.SDContextTag.TAG2LENGTH4;
import static org.slf4j.LoggerFactory.getLogger;

public class WhoHasCommand {
    private static final Logger log = getLogger(WhoHasCommand.class);

    private DatagramSocket socket;
    public static final int BACNET_DEFAULT_PORT = 47808;
    private byte[] buf = new byte[2048];
    private InetAddress sendToAddress;

    public WhoHasCommand() throws SocketException {
        socket = new DatagramSocket(null);
        socket.setBroadcast(true);
        socket.setReuseAddress(true);
    }

    void broadcast() throws IOException {
        local("255.255.255.255");
    }

    void local(String ipv4Address) throws IOException {
        SocketAddress inetAddress = new InetSocketAddress(BACNET_DEFAULT_PORT);
        sendToAddress = InetAddress.getByName(ipv4Address);
        socket.bind(inetAddress);
        sendWhoHas();
    }

    private void sendWhoHas() throws IOException {

        /*
        BVLC 810b000c
        NPUDU 0120ffff00ff
        X'10' PDU Type=1 (Unconfirmed-Service-Request-PDU)
        X'07' Service Choice=7 (Who-Has-Request)
        X'2C' SD Context Tag 2 (Object Identifier, L=4)
        X'00000001' Analog Input, Instance Number=1
         */
        ObjectId deviceSensorId = new ObjectId(ObjectType.AnalogValue, "1");
        String supportedPropertyHex = ObjectIdMapper.toHexString(deviceSensorId);
        String apdu = "1007" + TAG2LENGTH4 + supportedPropertyHex;
        String npdu = "0120ffff00ff";
        int numberOfOctets = (apdu.length() + npdu.length() + 8) / 2;
        String messageLength = Integer.toHexString(numberOfOctets);
        if (numberOfOctets <= 255) {
            messageLength = "00" + messageLength;
        }
        String bvlc = "81" + BvlcFunction.OriginalBroadcastNpdu.getBvlcFunctionHex() + messageLength;

        String hexString = bvlc + npdu + apdu;
        log.debug("Hex to send: {}", hexString);

        buf = hexStringToByteArray(hexString);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, sendToAddress, BACNET_DEFAULT_PORT);
        log.debug("Sending: {}", packet);
        socket.send(packet);
    }

    private void disconnect() {
        if (socket != null && socket.isConnected()) {
            socket.disconnect();
            socket = null;
        }
    }

    public static void main(String[] args) {
        WhoHasCommand client = null;

        //Destination may also be fetched as the first program argument.
        String destination = null;
        if (args.length > 0) {
            destination = args[0];
        }
        try {
            client = new WhoHasCommand();
            if (destination == null) {
                client.broadcast();
            } else {
                client.local(destination);
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
