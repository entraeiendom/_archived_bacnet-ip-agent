package no.entra.bacnet.agent.parser;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class HexStringParser {
    private static final Logger log = getLogger(HexStringParser.class);
    private static final String BVLCC_TYPE = "81";

    public static boolean isBacnet(String hexString) {
        return (hexString != null && hexString.startsWith(BVLCC_TYPE));
    }
}
