package no.entra.bacnet.agent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Listen to Bacnet Ip traffic.
 * <p>
 * Later also listen to MQTT commands, and send these to Bacnet Ip.
 * @Deprecated not in use.
 */
public class Server extends Thread {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger( Server.class );

    public static final String DEFAULT_BIND_IP = "0.0.0.0";
    private static final String WILDCARD_LISTENER = "0.0.0.0";
    public static final int BACNET_DEFAULT_PORT = 47808;

    private long messageCount = 0;

    private final DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public Server() throws SocketException {
        socket = new DatagramSocket(BACNET_DEFAULT_PORT);
    }

    public void run() {
        running = true;

        while (running) {
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

            if (received.equals("end")) {
                running = false;
                continue;
            }
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

     void handlePacket(DatagramPacket packet, byte[] buffer) {
        String received = new String(packet.getData(),0,packet.getLength());
        log.info("Received packet: {}, in buffer: {}", received, buffer.toString());
        addMessageCount();
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

    public boolean isRunning() {
        return running;
    }

    void stoListening() {
        running = false;
    }

    public static void main(String[] args) throws SocketException {
        Server server = new Server();
        server.run();
    }

}

