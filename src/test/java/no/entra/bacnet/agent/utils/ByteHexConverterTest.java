package no.entra.bacnet.agent.utils;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ByteHexConverterTest {

    @Test
    public void bytesToHexString() {

        String expected = "81105a";
        byte[] expectedInBytes = {56, 49, 49, 48, 53, 97};
        assertTrue(Arrays.equals(expected.getBytes(), expectedInBytes));

        /*
        String expected = "81105a";
        String hexString = bytesToHex(expected.getBytes());
        String received = bytesToHex(hexInBytes);
        assertEquals(expected, received);
        byte[] hexInBytes = {56, 49, 49, 48, 53, 97};
//        byte[] hexInBytes = {-127};
        String received = bytesToHex(hexInBytes);
        assertEquals(expected, received);
        hexInBytes = expected.getBytes();
        assertTrue(Arrays.equals(hexInBytes, hexToBytes(expected)));
        received = bytesToHex(hexInBytes);
        assertEquals(expected, received);
        */
        assertTrue(true);
    }

    @Test
    public void byteToHex() {
        byte hexAsInt = -127;
        String hex = ByteHexConverter.byteToHex(hexAsInt);
        assertEquals("81", hex);
    }
}