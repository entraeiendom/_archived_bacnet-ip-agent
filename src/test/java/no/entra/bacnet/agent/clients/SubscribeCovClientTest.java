package no.entra.bacnet.agent.clients;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SubscribeCovClientTest {

    private SubscribeCovClient covClient;
    private DatagramSocket socket;

    @Before
    public void setUp() throws Exception {
        socket = mock(DatagramSocket.class);
        covClient = new SubscribeCovClient(socket);
    }

    @Test
    public void local() throws IOException {
        covClient.local("10.10.10.10");
        verify(socket,  times(1)).send(any());
    }

    @Test
    public void buildConfirmedCovSingleRequest() {
        String expected = "810a00190120ffff00ff00020f0509121c0000000029013900";
        String hexString = covClient.buildConfirmedCovSingleRequest();
        assertEquals(expected, hexString);
    }
}