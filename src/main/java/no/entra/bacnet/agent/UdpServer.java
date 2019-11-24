package no.entra.bacnet.agent;

import no.entra.bacnet.agent.parser.HexStringParser;
import no.entra.bacnet.agent.rec.ProcessRecordedFile;
import no.entra.bacnet.json.BacNetParser;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.*;

import static no.entra.bacnet.agent.parser.HexStringParser.hasValue;
import static no.entra.bacnet.agent.utils.ByteHexConverter.bytesToHex;
import static org.slf4j.LoggerFactory.getLogger;

public class UdpServer extends Thread {
    private static final Logger log = getLogger(UdpServer.class);
    private DatagramSocket socket;
    private boolean listening;
    private boolean recording;
    private byte[] buf = new byte[256]; //TODO 2048
    public static final int BACNET_DEFAULT_PORT = 47808;

    private long messageCount = 0;
    ProcessRecordedFile processRecordedFile = null;
    File recordingFile = null;

    public UdpServer() throws SocketException {
        socket = new DatagramSocket(null);
        socket.setBroadcast(true);
        socket.setReuseAddress(true);
        SocketAddress inetAddress = new InetSocketAddress(BACNET_DEFAULT_PORT);
        socket.bind(inetAddress);
        String path = "bacnet-hexstring-recording";
        recordingFile = new File(path);
        processRecordedFile = new ProcessRecordedFile(recordingFile);
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
            byte[] receivedBytes = packet.getData();
            int lenghtOfData = packet.getLength();
            String hexString = bytesToHex(receivedBytes);
            String received = new String(packet.getData(), 0, packet.getLength());
            addMessageCount();
            convertAndForward(hexString);
            sendReply(packet, received);
        }
        socket.close();
    }

    void convertAndForward(String hexString) {
        log.trace("Received message: {}", hexString);
        if(recording) {
            processRecordedFile.writeToFile(hexString);
        }
        String apduHexString = HexStringParser.findApduHexString(hexString);
        try {
            //TODO fix BacNetParser in constructor
            if (hasValue(apduHexString)) {
                String json = new BacNetParser().jasonFromApdu(apduHexString);
                log.debug("Apdu {}\n{}", hexString,json);
            } else {
                //#2 TODO write unknown hexString to mqtt topic
                log.debug("No Apdu found for: {}", hexString);
            }
        } catch (Exception e) {
            log.debug("Failed to build json from {}. Reason: {}", apduHexString, e.getMessage());
        }
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

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    public File getRecordingFile() {
        return recordingFile;
    }

    public void setRecordingFile(File recordingFile) {
        this.recordingFile = recordingFile;
    }
}
