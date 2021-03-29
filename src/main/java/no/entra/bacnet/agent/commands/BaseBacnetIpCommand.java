package no.entra.bacnet.agent.commands;

import no.entra.bacnet.Octet;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static no.entra.bacnet.agent.utils.ByteHexConverter.hexStringToByteArray;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class BaseBacnetIpCommand {
    private static final Logger log = getLogger(BaseBacnetIpCommand.class);
    public static final Octet PDU_TYPE = new Octet("00");
    public static final Octet DEFAULT_MAX_APDU_SIZE = new Octet("04"); //1024 Octets
    public static final Octet DEFAULT_INVOKE_ID = new Octet("00");
    public static final int BACNET_DEFAULT_PORT = 47808;

    private final Octet maxApduSize;
    private final Octet invokeId;
    private InetAddress sendToAddress;
    private DatagramSocket socket;

    public BaseBacnetIpCommand() {
        this.maxApduSize = DEFAULT_MAX_APDU_SIZE;
        this.invokeId = DEFAULT_INVOKE_ID;
    }

    public void withSendToAddress(InetAddress sendToAddress){
        this.sendToAddress = sendToAddress;
    }

    public void withSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public abstract String buildHexString();

    public void send() {
        byte[] buf = new byte[2048];
        try {
            socket = new DatagramSocket(null);

            socket.setBroadcast(true);
            socket.setReuseAddress(true);
            buf = hexStringToByteArray(buildHexString());
            DatagramPacket packet = new DatagramPacket(buf, buf.length, sendToAddress, BACNET_DEFAULT_PORT);
            log.debug("Sending: {}", packet);
            socket.send(packet);
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
