package no.entra.bacnet.agent.parser;

import no.entra.bacnet.ip.bvlc.Bvlc;
import no.entra.bacnet.ip.bvlc.BvlcParser;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class HexStringParser {
    private static final Logger log = getLogger(HexStringParser.class);
    private static final String BVLCC_TYPE = "81";

    public static boolean isBacnet(String hexString) {
        return (hexString != null && hexString.startsWith(BVLCC_TYPE));
    }

    public static int findNumberOfCharactersInMessage(String hexString) {
        int numberOfCharsBvllOnly = 0;
        int totalNumberOfCharInMessage = 0;
        Bvlc bvlc = BvlcParser.parseHex(hexString);
        if (bvlc != null) {
            char[] length = bvlc.getNumberOfBvllOctetsLength();
            if (length != null) {
                numberOfCharsBvllOnly = Integer.parseInt(new String(length), 16);
                totalNumberOfCharInMessage = numberOfCharsBvllOnly + 4;
            }
        }

        return totalNumberOfCharInMessage;
    }
}
