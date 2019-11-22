package no.entra.bacnet.agent.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

public class ByteHexConverter {

    public static String bytesToHexString(byte[] receivedBytes) {
        String hexString = new String(receivedBytes, StandardCharsets.US_ASCII);
        return hexString;
    }


    public static byte[] hexToBytes(String hexString) {
        byte[] bytes = hexString.getBytes(StandardCharsets.UTF_8);
        return bytes;
    }
}
