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

    public UdpServer() throws SocketException {
        socket = new DatagramSocket(4445);
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
            String received
                    = new String(packet.getData(), 0, packet.getLength());

//            if (received.equals("end")) {
//                running = false;
//                continue;
//            }
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }
}
