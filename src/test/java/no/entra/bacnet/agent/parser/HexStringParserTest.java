package no.entra.bacnet.agent.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HexStringParserTest {

    private final String I_AM_REQUEST = "810b00070180000000000000";
    private final String APDU_REQUEST = "810b00340100100209001c020004d22c020004d239004e09702e91002f09cb2e2ea4770b1605b40f06303b2f2f09c42e91002f4f0000";

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

    @Test
    public void hasApdu() {
        assertFalse(HexStringParser.hasApdu(I_AM_REQUEST));
        assertTrue(HexStringParser.hasApdu(APDU_REQUEST));
    }
}