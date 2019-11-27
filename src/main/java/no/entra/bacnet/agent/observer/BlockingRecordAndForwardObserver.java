package no.entra.bacnet.agent.observer;

import no.entra.bacnet.agent.parser.HexStringParser;
import no.entra.bacnet.agent.recording.BacnetHexStringRecorder;
import no.entra.bacnet.json.BacNetParser;
import org.slf4j.Logger;

import static no.entra.bacnet.agent.parser.HexStringParser.hasValue;
import static org.slf4j.LoggerFactory.getLogger;

public class BlockingRecordAndForwardObserver implements BacnetObserver {
    private static final Logger log = getLogger(BlockingRecordAndForwardObserver.class);
    private boolean recording = false;
    private final BacnetHexStringRecorder hexStringRecorder;

    public BlockingRecordAndForwardObserver(BacnetHexStringRecorder hexStringRecorder) {
        this.hexStringRecorder = hexStringRecorder;
        recording = true;
    }

    @Override
    public void bacnetHexStringReceived(String hexString) {
        if(recording) {
            hexStringRecorder.persist(hexString);
        }
        String apduHexString = HexStringParser.findApduHexString(hexString);
        try {
            //TODO fix BacNetParser in constructor
            if (hasValue(apduHexString)) {
                String json = new BacNetParser().jasonFromApdu(apduHexString);
                log.debug("Apdu {}\n{}", hexString,json);
            } else {
                //#2 TODO write unknown hexString to mqtt topic
                log.debug("No Apdu found for: {}", hexString);
            }
        } catch (Exception e) {
            log.debug("Failed to build json from {}. Reason: {}", apduHexString, e.getMessage());
        }
    }
}
