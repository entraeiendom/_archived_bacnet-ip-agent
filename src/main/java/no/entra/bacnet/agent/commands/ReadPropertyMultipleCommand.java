package no.entra.bacnet.agent.commands;

import no.entra.bacnet.Octet;
import no.entra.bacnet.agent.sensors.SensorWithProperties;

import java.util.ArrayList;
import java.util.List;

import static no.entra.bacnet.json.utils.HexUtils.octetFromInt;

public class ReadPropertyMultipleCommand extends BaseBacnetIpCommand {

    public static final Octet PDU_TYPE = new Octet("00");
    public static final Octet DEFAULT_MAX_APDU_SIZE = new Octet("04"); //1024 Octets
    public static final Octet DEFAULT_INVOKE_ID = new Octet("00");

    private final Octet maxApduSize;
    private final Octet invokeId;

    public ReadPropertyMultipleCommand() {
        this.maxApduSize = DEFAULT_MAX_APDU_SIZE;
        this.invokeId = DEFAULT_INVOKE_ID;
    }

    /**
     *
     * @param invokeIdInt Range of 0-255
     */
    public ReadPropertyMultipleCommand(int invokeIdInt) {
        if (invokeIdInt < 0 || invokeIdInt > 255) {
            throw new IllegalArgumentException("InvokeId must be an integer in the range of 0-255. When above 255 please restart " +
                    "the counter on 0.");
        }
        this.maxApduSize = DEFAULT_MAX_APDU_SIZE;
        this.invokeId = octetFromInt(invokeIdInt);
    }

    private final List<SensorWithProperties> requestedProperties = new ArrayList<>();

    public String buildHexString() {
        return "" + PDU_TYPE + maxApduSize + invokeId;
    }
}
