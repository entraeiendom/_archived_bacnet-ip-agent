package no.entra.bacnet.agent.commands;

import no.entra.bacnet.agent.commands.properties.ReadPropertyMultipleCommand;

public class DevicePropertiesCommand extends ReadPropertyMultipleCommand {

    private final String deviceIpAddress;
    private int invokeId = 0; //0-255;

    //FIXME
    public DevicePropertiesCommand(String deviceIpAddress) {
        this.deviceIpAddress = deviceIpAddress;
    }

    public DevicePropertiesCommand(String deviceIpAddress, int invokeId) {
        this.deviceIpAddress = deviceIpAddress;
        this.invokeId = invokeId;
    }

    public String buildHexString() {
        return "810a001901040275010e0c020000011e094d0962094609a71f";
    }
}
