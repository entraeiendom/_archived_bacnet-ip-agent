package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.Octet;
import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.ObjectType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UnConfirmedSubscribeCovCommandTest {

    private UnConfirmedSubscribeCovCommand covCommand;
    private DatagramSocket socket;

    @Before
    public void setUp() throws Exception {
        socket = mock(DatagramSocket.class);
        ObjectId analogInput0 = new ObjectId(ObjectType.AnalogInput, "0");
        InetAddress sendToAddress = SubscribeCovCommand.inetAddressFromString("10.10.10.10");
        covCommand = new UnConfirmedSubscribeCovCommand(socket, sendToAddress, analogInput0);
        covCommand.setSubscriptionId(new Octet("12"));
        covCommand.setInvokeId(new Octet("0f"));

    }

    @Test
    public void executeTest() throws IOException {
        covCommand.execute();
        verify(socket,  times(1)).send(any());
    }

    @Test
    public void buildConfirmedCovSingleRequest() {
        String expected = "810a00190120ffff00ff00020f0509121c0000000029003900";
        String hexString = covCommand.buildHexString();
        assertEquals(expected, hexString);
    }
}