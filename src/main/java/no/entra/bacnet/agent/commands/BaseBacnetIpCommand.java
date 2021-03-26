package no.entra.bacnet.agent.commands;

import no.entra.bacnet.Octet;

import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class BaseBacnetIpCommand {
    public static final Octet PDU_TYPE = new Octet("00");
    public static final Octet DEFAULT_MAX_APDU_SIZE = new Octet("04"); //1024 Octets
    public static final Octet DEFAULT_INVOKE_ID = new Octet("00");

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

    }
}
