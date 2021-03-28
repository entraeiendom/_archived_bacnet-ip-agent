package no.entra.bacnet.agent.utils;

import no.entra.bacnet.json.utils.HexUtils;

public class ByteHexConverter {

    public static String integersToHex(byte[] receivedBytes) {
        String hexString = "";
        for (byte receivedByte : receivedBytes) {
            hexString += integerByteToHex(receivedByte);
        }
        return hexString;
    }

    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    public static String integerByteToHex(byte hexAsByte) {
        String hex = String.format("%02x", hexAsByte);
        return hex;
    }

    public static int findMessageLength(String bacnetMessageInHex) {
        String lenghtHex = bacnetMessageInHex.substring(4,8);
        int length = HexUtils.toInt(lenghtHex);
        return length * 2;
        /*
        String apdu = "1007" + TAG2LENGTH4 + "00000000";
        String npdu = "0120ffff00ff";
        int numberOfOctets = (apdu.length() + npdu.length() + 8) / 2;
        String messageLength = Integer.toHexString(numberOfOctets);
        if (numberOfOctets <= 255) {
            messageLength = "00" + messageLength;
        }
        return numberOfOctets * 2;

         */
    }
}
