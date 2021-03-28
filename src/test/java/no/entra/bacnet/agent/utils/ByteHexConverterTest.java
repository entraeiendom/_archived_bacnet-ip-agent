package no.entra.bacnet.agent.utils;

import org.junit.Test;

import java.util.Arrays;

import static no.entra.bacnet.agent.utils.ByteHexConverter.findMessageLength;
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

    @Test
    public void findMessageLengthTest() {
        String expectedContent = "810a002a01040005020109121c020003e92c0080000139004e09552e44400000002f096f2e8204002f4f";
        String bacnetMessageAsHex = expectedContent + "00000000000000000000000000000000000";
        assertEquals(expectedContent.length(), findMessageLength(bacnetMessageAsHex));

    }
}