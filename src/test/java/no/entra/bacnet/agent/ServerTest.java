package no.entra.bacnet.agent;

import no.entra.bacnet.agent.echo.EchoClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ServerTest {
    EchoClient client;
    UdpServer udpServer;

    @Before
    public void setup() throws SocketException, UnknownHostException {
        udpServer = new UdpServer();
        udpServer.start();
        client = new EchoClient();
    }

    @Test
    public void whenCanSendAndReceivePacket_thenCorrect() throws IOException {
        String echo = client.sendEcho("hello server");
        assertEquals("hello server", echo);
        echo = client.sendEcho("server is working");
        assertFalse(echo.equals("hello server"));
    }

    @After
    public void tearDown() throws IOException {
        udpServer.setRunning(false);
//        client.sendEcho("end");
        client.close();
    }

}
