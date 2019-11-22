package no.entra.bacnet.agent;

import org.slf4j.Logger;

import java.net.SocketException;

import static org.slf4j.LoggerFactory.getLogger;

public class AgentDeamon {
    private static final Logger log = getLogger(AgentDeamon.class);

    public static void main(String[] args) {
        try {
            UdpServer udpServer = new UdpServer();
            udpServer.setListening(true);
            udpServer.setRecording(true);
            udpServer.start();
        } catch (SocketException e) {
            log.error("Failed to run udpServer.", e);
        }
    }
}
