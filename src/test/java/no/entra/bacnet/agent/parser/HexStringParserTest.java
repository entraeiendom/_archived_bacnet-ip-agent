package no.entra.bacnet.agent.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HexStringParserTest {

    private final String I_AM_REQUEST = "810b00070180000000000000";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void isBacnet() {
        assertTrue(HexStringParser.isBacnet(I_AM_REQUEST));
    }

    @Test
    public void findNumberOfCharactersInMessage() {
        int numberOChars = HexStringParser.findNumberOfCharactersInMessage(I_AM_REQUEST);
        assertEquals(11, numberOChars);
    }

    @Test
    public void findBacnetIpMessage() {
        String expected = "810b0007018";
        String bacnetIpMessage = HexStringParser.findBacnetIpMessage(I_AM_REQUEST);
        assertEquals(expected, bacnetIpMessage);
    }
}