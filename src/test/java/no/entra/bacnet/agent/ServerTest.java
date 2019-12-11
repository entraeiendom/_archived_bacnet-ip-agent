package no.entra.bacnet.agent;

import no.entra.bacnet.agent.observer.BacnetObserver;
import no.entra.bacnet.agent.observer.BlockingRecordAndForwardObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class ServerTest {
    BacnetTestClient client;
    BacnetObserver bacnetObserver;
    UdpServer udpServer;

    @Before
    public void setup() throws SocketException, UnknownHostException {
        bacnetObserver =  mock(BlockingRecordAndForwardObserver.class);
        udpServer = new UdpServer(bacnetObserver);
        udpServer.start();
        client = new BacnetTestClient();
    }

    @Test
    public void whenCanSendAndReceivePacket_thenCorrect() throws IOException {
        assertEquals(0, udpServer.getMessageCount());
        String reply = client.sendBacnetWithReply("hello server");
        assertEquals("hello server", reply);
        reply = client.sendBacnetWithReply("hello server is working");
        assertFalse(reply.equals("hello server"));
        assertEquals(2, udpServer.getMessageCount());
    }

    @After
    public void tearDown() throws IOException {
        udpServer.setListening(false);
        client.close();
    }

}
