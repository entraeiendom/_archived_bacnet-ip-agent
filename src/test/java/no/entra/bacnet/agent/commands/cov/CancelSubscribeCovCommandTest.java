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

public class CancelSubscribeCovCommandTest {

    private CancelSubscribeCovCommand covCommand;
    private DatagramSocket socket;

    @Before
    public void setUp() throws Exception {
        socket = mock(DatagramSocket.class);
        ObjectId analogValue1 = new ObjectId(ObjectType.AnalogValue, "1");
        InetAddress sendToAddress = SubscribeCovCommand.inetAddressFromString("10.10.10.10");
        Octet subscriptionIdToCancel = new Octet("12");
        covCommand = new CancelSubscribeCovCommand(socket, sendToAddress, analogValue1,subscriptionIdToCancel );
        covCommand.setInvokeId(new Octet("01"));

    }

    @Test
    public void executeTest() throws IOException {
        covCommand.execute();
        verify(socket,  times(1)).send(any());
    }

    @Test
    public void buildCancelSubscribeCovHexString() {
        String expected = "810a00150120ffff00ff0002010509121c00800001";
        String hexString = covCommand.buildHexString();
        assertEquals(expected, hexString);
    }
}