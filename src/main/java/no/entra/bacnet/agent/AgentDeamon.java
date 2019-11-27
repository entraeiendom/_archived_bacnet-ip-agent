package no.entra.bacnet.agent;

import no.entra.bacnet.agent.observer.BacnetObserver;
import no.entra.bacnet.agent.observer.BlockingRecordAndForwardObserver;
import no.entra.bacnet.agent.recording.BacnetHexStringRecorder;
import no.entra.bacnet.agent.recording.FileBacnetHexStringRecorder;
import org.slf4j.Logger;

import java.io.File;
import java.net.SocketException;

import static org.slf4j.LoggerFactory.getLogger;

public class AgentDeamon {
    private static final Logger log = getLogger(AgentDeamon.class);

    public static void main(String[] args) {
        try {
            String path = "bacnet-hexstring-recording.log";
            File recordingFile = new File(path);
            BacnetHexStringRecorder hexStringRecorder = new FileBacnetHexStringRecorder(recordingFile);
            BacnetObserver bacnetObserver = new BlockingRecordAndForwardObserver(hexStringRecorder);
            UdpServer udpServer = new UdpServer(bacnetObserver);
            udpServer.setListening(true);
            udpServer.setRecording(true);
            udpServer.start();
        } catch (SocketException e) {
            log.error("Failed to run udpServer.", e);
        }
    }
}
