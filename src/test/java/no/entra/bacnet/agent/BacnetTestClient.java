package no.entra.bacnet.agent;

import java.io.IOException;
import java.net.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BacnetTestClient {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public BacnetTestClient() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    public void sendBacnet(String bacnetHexString) throws IOException {
        buf = bacnetHexString.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, Server.BACNET_DEFAULT_PORT);
        socket.send(packet);
    }

    public void close() {
        socket.close();
    }
}