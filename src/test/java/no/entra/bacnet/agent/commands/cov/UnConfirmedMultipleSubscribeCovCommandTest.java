package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.ObjectType;
import org.junit.Before;
import org.junit.Test;

import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class UnConfirmedMultipleSubscribeCovCommandTest {

    private UnConfirmedMultipleSubscribeCovCommand covCommand;
    private DatagramSocket socket;
    @Before
    public void setUp() throws Exception {
        socket = mock(DatagramSocket.class);

        ObjectId deviceSensorId1 = new ObjectId(ObjectType.AnalogValue, "1");
        ObjectId deviceSensorId2 = new ObjectId(ObjectType.AnalogValue, "0");
        InetAddress sendToAddress = SubscribeCovCommand.inetAddressFromString("10.10.10.10");
        covCommand = new UnConfirmedMultipleSubscribeCovCommand(socket, sendToAddress, deviceSensorId1, deviceSensorId2);
    }

    @Test
    public void buildHexString() {
        String expected = "810a00420120ffff00ff00020f1e09121901293c39054e0c008000011e0e09550f1c3f80000029010e09670f29001f0c008000001e0e09550f1c3f80000029011f4f";
        String hexString = covCommand.buildHexString();
        assertEquals(expected, hexString);
    }
}