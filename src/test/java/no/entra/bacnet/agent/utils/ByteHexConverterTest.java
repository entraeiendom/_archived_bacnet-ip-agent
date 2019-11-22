package no.entra.bacnet.agent.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ByteHexConverterTest {

    @Test
    public void bytesToHexString() {

        String expected = "81105a";
        byte[] hexInBytes = {56, 49, 49, 48, 53, 97};
        String received = ByteHexConverter.bytesToHexString(hexInBytes);
        assertEquals(expected, received);
        hexInBytes = ByteHexConverter.hexToBytes(expected);
        received = ByteHexConverter.bytesToHexString(hexInBytes);
        assertEquals(expected, received);
    }
}