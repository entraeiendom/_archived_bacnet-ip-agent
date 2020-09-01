package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.ObjectType;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.*;

import static no.entra.bacnet.agent.utils.ByteHexConverter.hexStringToByteArray;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class SubscribeCovCommand {
    private static final Logger log = getLogger(SubscribeCovCommand.class);

    private DatagramSocket socket;
    public static final int BACNET_DEFAULT_PORT = 47808;
    private byte[] buf = new byte[2048];
    private InetAddress sendToAddress;

    public SubscribeCovCommand() throws SocketException {
        socket = new DatagramSocket(null);
        socket.setBroadcast(true);
        socket.setReuseAddress(true);
    }

    protected SubscribeCovCommand(DatagramSocket socket) {
        this.socket = socket;
    }

    void broadcast() throws IOException {
        ObjectId analogInput0 = new ObjectId(ObjectType.AnalogInput, "0");
        local("255.255.255.255", analogInput0);
    }

    void local(String ipv4Address, ObjectId parameterToWatch) throws IOException {
        SocketAddress inetAddress = new InetSocketAddress(BACNET_DEFAULT_PORT);
        sendToAddress = InetAddress.getByName(ipv4Address);
        socket.bind(inetAddress);
        sendSubscribeCov(parameterToWatch);
    }

    protected void sendSubscribeCov(ObjectId parameterToWatch) throws IOException {
        String hexString = buildHexString(parameterToWatch  );
        log.debug("Send subscribeCovHex {} to address: {}", hexString, sendToAddress);
        buf = hexStringToByteArray(hexString);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, sendToAddress, BACNET_DEFAULT_PORT);
        log.debug("Sending: {}", packet);
        socket.send(packet);
    }

    protected abstract String buildHexString(ObjectId deviceSensorId) ;


    void disconnect() {
        if (socket != null && socket.isConnected()) {
            socket.disconnect();
            socket = null;
        }
    }

    public static void main(String[] args) {
        SubscribeCovCommand client = null;

        //Destination may also be fetched as the first program argument.
        String destination = null;
        if (args.length > 0) {
            destination = args[0];
        }
        try {
//            client = new ConfirmedSubscribeCovCommand();
//            client = new UnconfirmedSubscribeCovCommand();
            client = new UnconfirmedMultipleSubscribeCovCommand();
            ObjectId parameterToWatch = new ObjectId(ObjectType.AnalogValue, "1");
            if (destination == null) {
                client.broadcast();
            } else {
                client.local(destination, parameterToWatch);
            }
            Thread.sleep(10000);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            client.disconnect();
        }
    }
}
