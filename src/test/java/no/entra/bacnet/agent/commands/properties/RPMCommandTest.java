package no.entra.bacnet.agent.commands.properties;

import no.entra.bacnet.json.objects.ObjectId;
import no.entra.bacnet.json.objects.ObjectType;
import no.entra.bacnet.json.objects.PropertyIdentifier;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertNotNull;

public class RPMCommandTest {

    @Test
    public void buildHexString() throws UnknownHostException {
        InetAddress sendToAddress = InetAddress.getByName("127.0.0.1");
        ObjectId device8 = new ObjectId(ObjectType.Device,"8");
        RPMCommand readPropertyMultipleCommand = new RPMCommand.RPMCommandBuilder(sendToAddress)
                .withInvokeId(1)
                .withObjectId(device8)
                .withPropertyIdentifier(PropertyIdentifier.ObjectName)
                .withPropertyIdentifier(PropertyIdentifier.ProtocolVersion)
                .withPropertyIdentifier(PropertyIdentifier.ProtocolRevision)
                .build();
        assertNotNull(readPropertyMultipleCommand);

    }
}