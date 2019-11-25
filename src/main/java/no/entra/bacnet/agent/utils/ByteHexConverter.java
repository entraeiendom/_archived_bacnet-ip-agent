package no.entra.bacnet.agent.utils;

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
}
