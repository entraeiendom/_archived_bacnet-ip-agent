package no.entra.bacnet.agent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpServer extends Thread {
    private DatagramSocket socket;
    private boolean listening;
    private byte[] buf = new byte[256];
    public static final int BACNET_DEFAULT_PORT = 47808;

    private long messageCount = 0;

    public UdpServer() throws SocketException {
        socket = new DatagramSocket(BACNET_DEFAULT_PORT);
    }

    public void run() {
        listening = true;

        while (listening) {
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received = new String(packet.getData(), 0, packet.getLength());
            addMessageCount();
            sendReply(packet, received);
        }
        socket.close();
    }

    void sendReply(DatagramPacket packet, String received) {
        boolean sendReply = expectingReply(received);
        if (sendReply) {
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean expectingReply(String received) {
        boolean expectingReply = false;
        if (received != null && received.startsWith("hello")) {
            expectingReply = true;
        }
        return expectingReply;
    }

    void addMessageCount() {
        if (messageCount < Long.MAX_VALUE) {
            messageCount ++;
        } else {
            messageCount = 1;
        }
    }

    public long getMessageCount() {
        return messageCount;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }
}
