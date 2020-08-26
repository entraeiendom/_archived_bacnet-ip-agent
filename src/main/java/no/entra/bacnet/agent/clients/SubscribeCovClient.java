package no.entra.bacnet.agent.clients;

import no.entra.bacnet.json.bvlc.BvlcFunction;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.*;

import static no.entra.bacnet.agent.utils.ByteHexConverter.hexStringToByteArray;
import static org.slf4j.LoggerFactory.getLogger;

public class SubscribeCovClient {
    private static final Logger log = getLogger(SubscribeCovClient.class);

    private DatagramSocket socket;
    public static final int BACNET_DEFAULT_PORT = 47808;
    private byte[] buf = new byte[2048];
    private InetAddress sendToAddress;

    public SubscribeCovClient() throws SocketException {
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
        sendWhoIs();
    }

    private void sendWhoIs() throws IOException {

        String analogInput0 = "00000000";
        String apdu = "00020f0509121c" + analogInput0 + "29013900";
        String npdu = "0120ffff00ff";
        int numberOfOctets = (apdu.length() + npdu.length() + 8) / 2;
        String messageLength = Integer.toHexString(numberOfOctets);
        if (numberOfOctets <= 255) {
            messageLength = "00" + messageLength;
        }
        String bvlc = "81" + BvlcFunction.OriginalUnicastNpdu.getBvlcFunctionHex() + messageLength;

        String hexString = bvlc.toString() + npdu.toString() + apdu;
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
        SubscribeCovClient client = null;

        //Destination may also be fetched as the first program argument.
        String destination = null;
        if (args.length > 0) {
            destination = args[0];
        }
        try {
            client = new SubscribeCovClient();
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
