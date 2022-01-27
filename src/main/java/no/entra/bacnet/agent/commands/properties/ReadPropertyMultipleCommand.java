package no.entra.bacnet.agent.commands.properties;

import no.entra.bacnet.agent.commands.BaseBacnetIpCommand;
import no.entra.bacnet.agent.devices.DeviceId;
import no.entra.bacnet.agent.sensors.SensorWithProperties;
import no.entra.bacnet.internal.properties.PropertyIdentifier;
import no.entra.bacnet.json.bvlc.BvlcFunction;
import no.entra.bacnet.octet.Octet;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import static no.entra.bacnet.agent.utils.ByteHexConverter.hexStringToByteArray;
import static no.entra.bacnet.internal.apdu.SDContextTag.*;
import static no.entra.bacnet.utils.HexUtils.intToHexString;
import static no.entra.bacnet.utils.HexUtils.octetFromInt;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @Deprecated Prefer to use RPMCommand
 */
public class ReadPropertyMultipleCommand extends BaseBacnetIpCommand {
    private static final Logger log = getLogger(ReadPropertyMultipleCommand.class);
    public static final Octet PDU_TYPE = new Octet("00");
    public static final Octet DEFAULT_MAX_APDU_SIZE = new Octet("04"); //1024 Octets
    public static final Octet DEFAULT_INVOKE_ID = new Octet("00");

    private final Octet maxApduSize;
    private final Octet invokeId;

    private DatagramSocket socket;
    public static final int BACNET_DEFAULT_PORT = 47808;
    private byte[] buf = new byte[2048];
    private InetAddress sendToAddress;

    public ReadPropertyMultipleCommand() {
        this.maxApduSize = DEFAULT_MAX_APDU_SIZE;
        this.invokeId = DEFAULT_INVOKE_ID;
    }

    /**
     *
     * @param invokeIdInt Range of 0-255
     */
    public ReadPropertyMultipleCommand(int invokeIdInt) throws SocketException {
        if (invokeIdInt < 0 || invokeIdInt > 255) {
            throw new IllegalArgumentException("InvokeId must be an integer in the range of 0-255. When above 255 please restart " +
                    "the counter on 0.");
        }
        this.maxApduSize = DEFAULT_MAX_APDU_SIZE;
        this.invokeId = octetFromInt(invokeIdInt);
        socket = new DatagramSocket(null);
        socket.setBroadcast(true);
        socket.setReuseAddress(true);
    }

    public void local(DeviceId deviceId) throws IOException {
        SocketAddress inetAddress = new InetSocketAddress(BACNET_DEFAULT_PORT);
        String ipv4Address = deviceId.getIpAddress();
        sendToAddress = InetAddress.getByName(ipv4Address);
        socket.bind(inetAddress);
        Integer instanceNumber = deviceId.getInstanceNumber();
        sendReadPropertyMultipleRequest(instanceNumber);
    }

    private void sendReadPropertyMultipleRequest(Integer instanceNumber) throws IOException {

        String hexString = buildHexString(instanceNumber);
        log.debug("Hex to send: {}", hexString);

        buf = hexStringToByteArray(hexString);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, sendToAddress, BACNET_DEFAULT_PORT);
        log.debug("Sending: {}", packet);
        socket.send(packet);
        socket.close();
    }

    protected String buildHexString(Integer instanceNumber) {
        if (instanceNumber == null ) {
            instanceNumber = 1;
        }
        String apdu = "1007" + TAG2LENGTH4 + "00000000";

        String npdu = "0104"; //0104
        int numberOfOctets = (apdu.length() + npdu.length() + 8) / 2;
        String messageLength = Integer.toHexString(numberOfOctets);
        if (numberOfOctets <= 255) {
            messageLength = "00" + messageLength;
        }
        String bvlc = "81" + BvlcFunction.OriginalUnicastNpdu.getBvlcFunctionHex() + messageLength;

        String invokeId = new Octet("00").toString();
        String serviceChoice = new Octet("0c").toString();
        String objectIdentifier = TAG0LENGTH4.toString(); //0c02000008
        int deviceId = Integer.valueOf(instanceNumber);
        String instanceNumberHexString = "02" + intToHexString(deviceId,6);
        String propertyIdentifier =  TAG1LENGTH1 + PropertyIdentifier.ProtocolServicesSupported.getPropertyIdentifierHex(); //"1961";
        apdu = "0275" + invokeId + serviceChoice + objectIdentifier + instanceNumberHexString + propertyIdentifier;
        apdu = "38d135030732f875a421c3e1080045000033d58f40004011defbc0a80268c0a80276bac0bac0001f865f810a001701040275010e0c020000081e094d0962098b1f";
        apdu = "0275010e0c020000081e094d0962098b1f";
        npdu = "0104";
        bvlc = "810a0017";
        String hexString = bvlc + npdu + apdu;
        return hexString;
    }

    private final List<SensorWithProperties> requestedProperties = new ArrayList<>();

    public String buildHexString() {
        return "" + PDU_TYPE + maxApduSize + invokeId;
    }
}
