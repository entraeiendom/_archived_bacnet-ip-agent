package no.entra.bacnet.agent.commands;

import no.entra.bacnet.agent.devices.DeviceId;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ServicesSupportedCommandTest {

    @Test
    public void buildHexString() throws IOException {
        DeviceId deviceId =  new DeviceId();
        deviceId.setIpAddress("127.0.0.1");
        deviceId.setInstanceNumber(8);
        ServicesSupportedCommand command = new ServicesSupportedCommand();
        String hexString = command.buildHexString(deviceId.getInstanceNumber());
        assertEquals("810a001101040275000c0c020000081961", hexString);
    }
}