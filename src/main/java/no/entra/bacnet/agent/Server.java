package no.entra.bacnet.agent;

import java.net.*;

/**
 * Listen to Bacnet Ip traffic.
 * <p>
 * Later also listen to MQTT commands, and send these to Bacnet Ip.
 */
public class Server implements Runnable {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger( Server.class );

    public static final int DEFAULT_PORT = 47808;
    public static final String DEFAULT_BIND_IP = "0.0.0.0";
    private static final String WILDCARD_LISTENER = "0.0.0.0";

    public void run() {
        DatagramSocket socket = null;
        DatagramPacket packet = null;
        byte[] buffer = null;

        try {
            int listenPort = 48707;
            InetAddress bindToAddress = InetAddress.getByName("192.168.1.31");
            socket = new DatagramSocket( listenPort, bindToAddress);
            socket.setReuseAddress(true);
            buffer = new byte[ 2048 ];
            packet = new DatagramPacket( buffer, buffer.length );
        } catch (SocketException | UnknownHostException e ) {
            log.error( "Could not open the socket: \n" + e.getMessage() );
            System.exit( 1 );
        }
        boolean isRunning = true;
        while ( isRunning ) {
            try {
                socket.receive( packet );
                handlePacket( packet, buffer );
            } catch ( Exception e ) {
                log.error( e.getMessage() );
            }
        }
    }

     void handlePacket(DatagramPacket packet, byte[] buffer) {
        log.info("Received packet: {}, in buffer: {}", packet, buffer.toString());
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

}

