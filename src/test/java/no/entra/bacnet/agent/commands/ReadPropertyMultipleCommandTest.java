package no.entra.bacnet.agent.commands;

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
    }
}