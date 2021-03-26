package no.entra.bacnet.agent.commands;

import no.entra.bacnet.Octet;
import no.entra.bacnet.agent.devices.DeviceId;
import no.entra.bacnet.json.bvlc.BvlcFunction;
import no.entra.bacnet.json.objects.PropertyIdentifier;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.*;

import static no.entra.bacnet.agent.utils.ByteHexConverter.hexStringToByteArray;
import static no.entra.bacnet.json.apdu.SDContextTag.*;
import static no.entra.bacnet.json.utils.HexUtils.intToHexString;
import static org.slf4j.LoggerFactory.getLogger;

public class ServicesSupportedCommand {
    private static final Logger log = getLogger(ServicesSupportedCommand.class);

    private DatagramSocket socket;
    public static final int BACNET_DEFAULT_PORT = 47808;
    private byte[] buf = new byte[2048];
    private InetAddress sendToAddress;

    public ServicesSupportedCommand() throws SocketException {
        socket = new DatagramSocket(null);
        socket.setBroadcast(true);
        socket.setReuseAddress(true);
    }

//    void broadcast() throws IOException {
//        local("255.255.255.255");
//        SocketAddress inetAddress = new InetSocketAddress(BACNET_DEFAULT_PORT);
//        String ipv4Address = "255.255.255.255";
//        sendToAddress = InetAddress.getByName(ipv4Address);
//        socket.bind(inetAddress);
//        sendWhoHas();
//    }

    public void local(DeviceId deviceId) throws IOException {
        SocketAddress inetAddress = new InetSocketAddress(BACNET_DEFAULT_PORT);
        String ipv4Address = deviceId.getIpAddress();
        sendToAddress = InetAddress.getByName(ipv4Address);
        socket.bind(inetAddress);
        Integer instanceNumber = deviceId.getInstanceNumber();
        sendServicesSupportedRequest(instanceNumber);
    }

    private void sendServicesSupportedRequest(Integer instanceNumber) throws IOException {

        String hexString = buildHexString(instanceNumber);
        log.debug("Hex to send: {}", hexString);

        buf = hexStringToByteArray(hexString);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, sendToAddress, BACNET_DEFAULT_PORT);
        log.debug("Sending: {}", packet);
        socket.send(packet);
    }

    protected String buildHexString(Integer instanceNumber) {
        if (instanceNumber == null ) {
            instanceNumber = 1;
        }
        String apdu = "1007" + TAG2LENGTH4 + "00000000";

        String npdu = "0104";
        int numberOfOctets = (apdu.length() + npdu.length() + 8) / 2;
        String messageLength = Integer.toHexString(numberOfOctets);
        if (numberOfOctets <= 255) {
            messageLength = "00" + messageLength;
        }
        String bvlc = "81" + BvlcFunction.OriginalBroadcastNpdu.getBvlcFunctionHex() + messageLength;

        String invokeId = new Octet("00").toString();
        String serviceChoice = new Octet("0c").toString();
        String objectIdentifier = TAG0LENGTH4.toString(); //0c02000008
        int deviceId = Integer.valueOf(instanceNumber);
        String instanceNumberHexString = "02" + intToHexString(deviceId,6);
        String propertyIdentifier =  TAG1LENGTH1 + PropertyIdentifier.ProtocolServicesSupported.getPropertyIdentifierHex(); //"1961";
        apdu = "0275" + invokeId + serviceChoice + objectIdentifier + instanceNumberHexString + propertyIdentifier;
        npdu = "0104";
        bvlc = "810a0011";
        String hexString = bvlc + npdu + apdu;
        return hexString;
    }

    private void disconnect() {
        if (socket != null && socket.isConnected()) {
            socket.disconnect();
            socket = null;
        }
    }

    public static void main(String[] args) {
        ServicesSupportedCommand client = null;
        DeviceId deviceId;

        //Destination may also be fetched as the first program argument.
        String destination = null;
        if (args.length > 0) {
            destination = args[0];
            deviceId = new DeviceId("1");
            deviceId.setIpAddress(destination);

            try {
                client = new ServicesSupportedCommand();
                client.local(deviceId);
                Thread.sleep(1000);
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
}
