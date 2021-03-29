package no.entra.bacnet.agent.commands;

import no.entra.bacnet.agent.commands.properties.ReadPropertyMultipleCommand;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReadPropertyMultipleCommandTest {

    private ReadPropertyMultipleCommand readMultipleCommand;

    @Before
    public void setUp() throws Exception {
        readMultipleCommand = new ReadPropertyMultipleCommand();
    }

    @Test
    public void buildHexString() {
        assertEquals("000400", readMultipleCommand.buildHexString());
//        assertEquals("810a001901040275010e0c020000011e094d0962094609a71f", readMultipleCommand.buildHexString());
    }

    @Test
    public void objectNameProtocolVersionRevision() {

        //Device 8
        assertEquals("810a001701040275010e0c020000081e094d0962098b1f", readMultipleCommand.buildHexString());
        //Expected result is 810a0028010030010e0c020000081e294d4e75060046574643554f29624e21014f298b4e210e4f1f
    }
}