package no.entra.bacnet.agent.commands.cov;

import no.entra.bacnet.Octet;
import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.ObjectType;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;

public class SubscribeCovCommandBuilderTest {

    @Test
    public void buildCOVSingleSensorUnConfirmedTest() throws UnknownHostException {
        ObjectId analogInput0 = new ObjectId(ObjectType.AnalogInput, "0");
        InetAddress sendToAddress = SubscribeCovCommand.inetAddressFromString("10.10.10.10");
        SubscribeCovCommand covCommand = new SubscribeCovCommandBuilder(sendToAddress, analogInput0)
                .withSubscriptionId(new Octet("12"))
                .withInvokeId(new Octet("0f"))
                .withConfirmedNotifications(false)
                .build();
        String expected = "810a00190120ffff00ff00020f0509121c0000000029003900";
        String hexString = covCommand.buildHexString();
        assertEquals(expected, hexString);
    }
}