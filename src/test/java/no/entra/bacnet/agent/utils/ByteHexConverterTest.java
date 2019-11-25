package no.entra.bacnet.agent.utils;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ByteHexConverterTest {

    @Test
    public void bytesToHexString() {

        String expected = "81105a";
        byte[] expectedInHexBytes = {56, 49, 49, 48, 53, 97};
        assertTrue(Arrays.equals(expected.getBytes(), expectedInHexBytes));
        byte[] receivedUdpDataInIntegers = {-127, 16, 90};

        String hexString = ByteHexConverter.integersToHex(receivedUdpDataInIntegers);
        assertEquals(expected, hexString);


    }

    @Test
    public void integerByteToHex() {
        byte hexAsInt = -127;
        String hex = ByteHexConverter.integerByteToHex(hexAsInt);
        assertEquals("81", hex);
    }
}