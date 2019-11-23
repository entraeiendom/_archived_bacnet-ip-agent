package no.entra.bacnet.agent.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HexStringParserTest {

    private final String I_AM_REQUEST = "810b00070180000000000000";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void isBacnet() {
        assertTrue(HexStringParser.isBacnet(I_AM_REQUEST));
    }
}