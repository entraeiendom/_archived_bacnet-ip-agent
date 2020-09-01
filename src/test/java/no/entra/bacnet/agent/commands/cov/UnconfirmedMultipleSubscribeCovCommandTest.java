package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.ObjectType;
import org.junit.Before;
import org.junit.Test;

import java.net.DatagramSocket;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class UnconfirmedMultipleSubscribeCovCommandTest {

    private UnconfirmedMultipleSubscribeCovCommand covCommand;
    private DatagramSocket socket;
    @Before
    public void setUp() throws Exception {
        socket = mock(DatagramSocket.class);
        covCommand = new UnconfirmedMultipleSubscribeCovCommand(socket);
    }

    @Test
    public void buildHexString() {
        String expected = "810a00420120ffff00ff00020f1e09121901293c39054e0c008000011e0e09550f1c3f80000029010e09670f29001f0c008000001e0e09550f1c3f80000029011f4f";
        ObjectId deviceSensorId = new ObjectId(ObjectType.AnalogInput, "0");
        String hexString = covCommand.buildHexString(deviceSensorId);
        assertEquals(expected, hexString);
    }
}