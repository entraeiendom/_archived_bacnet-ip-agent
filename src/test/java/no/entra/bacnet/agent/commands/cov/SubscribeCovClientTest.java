package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.ObjectType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SubscribeCovClientTest {

    private SubscribeCovCommand covClient;
    private DatagramSocket socket;

    @Before
    public void setUp() throws Exception {
        socket = mock(DatagramSocket.class);
        covClient = new SubscribeCovCommand(socket);
    }

    @Test
    public void local() throws IOException {
        ObjectId analogInput0 = new ObjectId(ObjectType.AnalogInput, "0");
        covClient.local("10.10.10.10", analogInput0 );
        verify(socket,  times(1)).send(any());
    }

    @Test
    public void buildConfirmedCovSingleRequest() {
        String expected = "810a00190120ffff00ff00020f0509121c0000000029013900";
        ObjectId deviceSensorId = new ObjectId(ObjectType.AnalogInput, "0");
        String hexString = covClient.buildConfirmedCovSingleRequest(deviceSensorId);
        assertEquals(expected, hexString);
    }
}