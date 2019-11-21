package no.entra.bacnet.agent;

import java.io.IOException;
import java.net.*;

public class BacnetTestClient {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public BacnetTestClient() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    public String sendBacnetWithReply(String bacnetHexString) throws IOException {
        buf = bacnetHexString.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, Server.BACNET_DEFAULT_PORT);
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }
}