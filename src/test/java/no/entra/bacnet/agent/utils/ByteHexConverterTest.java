package no.entra.bacnet.agent.utils;

import org.junit.Test;

import java.util.Arrays;

import static no.entra.bacnet.agent.utils.ByteHexConverter.bytesToHex;
import static no.entra.bacnet.agent.utils.ByteHexConverter.hexToBytes;
import static org.junit.Assert.*;

public class ByteHexConverterTest {

    @Test
    public void bytesToHexString() {

        String expected = "81105a";
//        byte[] hexInBytes = {56, 49, 49, 48, 53, 97};
        byte[] hexInBytes = {-127};
        String received = bytesToHex(hexInBytes);
        assertEquals(expected, received);
        hexInBytes = hexToBytes(expected);
        assertTrue(Arrays.equals(hexInBytes, hexToBytes(expected)));
        received = bytesToHex(hexInBytes);
        assertEquals(expected, received);
    }

    @Test
    public void byteToHex() {
        byte hexAsInt = -127;
        String hex = ByteHexConverter.byteToHex(hexAsInt);
        assertEquals("81", hex);
    }
}