package no.entra.bacnet.agent.commands.properties;

import no.entra.bacnet.internal.properties.PropertyIdentifier;
import no.entra.bacnet.objects.ObjectId;
import no.entra.bacnet.objects.ObjectType;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RPMCommandTest {

    @Test
    public void buildHexString() throws UnknownHostException {
        InetAddress sendToAddress = InetAddress.getByName("127.0.0.1");
        ObjectId device8 = new ObjectId(ObjectType.Device,8);
        RPMCommand readPropertyMultipleCommand = new RPMCommand.RPMCommandBuilder(sendToAddress)
                .withInvokeId(1)
                .withObjectId(device8)
                .withPropertyIdentifier(PropertyIdentifier.ObjectName)
                .withPropertyIdentifier(PropertyIdentifier.ProtocolVersion)
                .withPropertyIdentifier(PropertyIdentifier.ProtocolRevision)
                .build();
        assertNotNull(readPropertyMultipleCommand);
        String expectedHexString = "810a001701040275010e0c020000081e094d0962098b1f";
        assertEquals(expectedHexString, readPropertyMultipleCommand.buildHexString());
    }
}