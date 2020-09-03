package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.Octet;
import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.ObjectType;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

import static no.entra.bacnet.agent.utils.ByteHexConverter.hexStringToByteArray;
import static no.entra.bacnet.json.utils.HexUtils.octetFromInt;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class SubscribeCovCommand {
    private static final Logger log = getLogger(SubscribeCovCommand.class);
    public static final String BROADCAST_IP = "255.255.255.255";
    private final ArrayList<ObjectId> subscribeToSensorIds;
    private final InetAddress sendToAddress;
    private final DatagramSocket socket;
    private Octet subscriptionId; //aka invokeId or tag to track all subsequent notifications.
    private Octet invokeId = new Octet("01");
    public static final int BACNET_DEFAULT_PORT = 47808;
    private byte[] buf = new byte[2048];



    public SubscribeCovCommand(InetAddress sendToAddress, ObjectId... subscribeToSensorIds) throws IOException {
        socket = new DatagramSocket(null);
        socket.setBroadcast(true);
        socket.setReuseAddress(true);
        SocketAddress inetAddress = new InetSocketAddress(BACNET_DEFAULT_PORT);
        socket.bind(inetAddress);
        this.sendToAddress = sendToAddress;
        this.subscribeToSensorIds = mapList(subscribeToSensorIds);
        int nextId = new Random().nextInt(255); //255 is max allowed
        subscriptionId = octetFromInt(nextId);
    }


    protected SubscribeCovCommand(DatagramSocket socket, InetAddress sendToAddress, ObjectId... subscribeToSensorIds) throws IOException {
        this.socket = socket;
        SocketAddress inetAddress = new InetSocketAddress(BACNET_DEFAULT_PORT);
        socket.bind(inetAddress);
        this.sendToAddress = sendToAddress;
        this.subscribeToSensorIds = mapList(subscribeToSensorIds);
        int nextId = new Random().nextInt(255); //255 is max allowed
        subscriptionId = octetFromInt(nextId);
    }

    private ArrayList<ObjectId> mapList(ObjectId[] objectIds) {
        ArrayList<ObjectId> objectIdList = new ArrayList<>();
        for (ObjectId objectId : objectIds) {
            objectIdList.add(objectId);
        }
        return objectIdList;
    }

    protected void sendSubscribeCov() throws IOException {
        String hexString = buildHexString();
        log.debug("Send subscribeCovHex {} to address: {}", hexString, sendToAddress);
        buf = hexStringToByteArray(hexString);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, sendToAddress, BACNET_DEFAULT_PORT);
        log.debug("Sending: {}", packet);
        socket.send(packet);
    }

    public static InetAddress inetAddressFromString(String ipv4Address) throws UnknownHostException {
        return InetAddress.getByName(ipv4Address);
    }

    public ArrayList<ObjectId> getSubscribeToSensorIds() {
        return subscribeToSensorIds;
    }

    protected abstract String buildHexString() ;

    public void execute() throws IOException {
        try {
            sendSubscribeCov();
        } catch (IOException e) {
            log.debug("Failed to send bacnet hex to {}. Reason: {}", sendToAddress, e.getMessage());
            throw e;
        }
    };


    void disconnect() {
        if (socket != null && socket.isConnected()) {
            socket.disconnect();
        }
    }

    public void setSubscriptionId(Octet id) {
        this.subscriptionId = id;
    }

    public Octet getSubscriptionId() {
        return subscriptionId;
    }

    public Octet getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(Octet invokeId) {
        this.invokeId = invokeId;
    }

    public static void main(String[] args) {
        SubscribeCovCommand covCommand = null;

        //Destination may also be fetched as the first program argument.
        String destination = BROADCAST_IP;
        if (args.length > 0) {
            destination = args[0];
        }
        try {
            ObjectId analogValue1 = new ObjectId(ObjectType.AnalogValue, "1");
            InetAddress sendToAddress = SubscribeCovCommand.inetAddressFromString(destination);
            covCommand = new ConfirmedSubscribeCovCommand(sendToAddress, analogValue1);
//            covCommand = new UnconfirmedSubscribeCovCommand();
//            ObjectId analogValue0 = new ObjectId(ObjectType.AnalogValue, "0");
//            covCommand = new UnconfirmedMultipleSubscribeCovCommand(sendToAddress, analogValue1, analogValue0);

            covCommand.sendSubscribeCov();
            Thread.sleep(10000);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            covCommand.disconnect();
        }
    }


}
