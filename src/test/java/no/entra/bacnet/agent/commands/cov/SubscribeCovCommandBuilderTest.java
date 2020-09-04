package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.Octet;
import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.ObjectType;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

public class SubscribeCovCommandBuilderTest {

    private ObjectId analogInput0;
    InetAddress sendToAddress;

    @Before
    public void setUp() throws Exception {
        analogInput0 = new ObjectId(ObjectType.AnalogInput, "0");
        sendToAddress = SubscribeCovCommand.inetAddressFromString("10.10.10.10");
    }

    @Test
    public void buildCOVSingleSensorUnConfirmedTest()  {
        String expected = "810a00190120ffff00ff00020f0509121c0000000029003900";
        SubscribeCovCommand covCommand = new SubscribeCovCommandBuilder(sendToAddress, analogInput0)
                .withSubscriptionId(new Octet("12"))
                .withInvokeId(new Octet("0f"))
                .withConfirmedNotifications(false)
                .build();
        assertTrue(covCommand instanceof UnconfirmedSubscribeCovCommand);
        String hexString = covCommand.buildHexString();
        assertEquals(expected, hexString);
    }

    @Test
    public void buildCOVSingleSensorConfirmedTest()  {
        String expected = "810a00190120ffff00ff00020f0509121c0000000029013900";
        SubscribeCovCommand covCommand = new SubscribeCovCommandBuilder(sendToAddress, analogInput0)
                .withSubscriptionId(new Octet("12"))
                .withInvokeId(new Octet("0f"))
                .withConfirmedNotifications(true)
                .withLifetime(50)
                .build();
        assertTrue(covCommand instanceof ConfirmedSubscribeCovCommand);
        String hexString = covCommand.buildHexString();
        assertEquals(expected, hexString);
    }

    @Test(expected = IllegalStateException.class)
    public void verifyLifetimeAndUnconfirmedIsIllegal()  {
        SubscribeCovCommand covCommand = new SubscribeCovCommandBuilder(sendToAddress, analogInput0)
                .withSubscriptionId(new Octet("12"))
                .withInvokeId(new Octet("0f"))
                .withLifetime(5)
                .withConfirmedNotifications(false)
                .build();
        fail("Should not be reached");
    }
}