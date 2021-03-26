package no.entra.bacnet.agent.importer;

import no.entra.bacnet.agent.commands.ServicesSupportedCommand;
import no.entra.bacnet.agent.commands.WhoIsCommand;
import no.entra.bacnet.agent.devices.DeviceId;
import no.entra.bacnet.agent.devices.DeviceIdService;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import static no.entra.bacnet.json.utils.StringUtils.hasValue;
import static org.slf4j.LoggerFactory.getLogger;

public class DeviceImporter {
    private static final Logger log = getLogger(DeviceImporter.class);

    private final DeviceIdService deviceIdService;

    public DeviceImporter(DeviceIdService deviceIdService) {
        this.deviceIdService = deviceIdService;
    }

    public void findDevices() {
        try {
            WhoIsCommand whoIsCommand = new WhoIsCommand();
            whoIsCommand.broadcast();
        } catch (SocketException e) {
            log.info("Failed to find devices. Reason is: {}", e.getMessage());
        } catch (IOException e) {
            log.info("Failed to find devices. Send WhoIs command threw: {}", e.getMessage());
        }
    }

    public void findSensorsAndPropertiesTheDevicesSupports() {
        List<DeviceId> devicesDetected = deviceIdService.allDevices();
//        for (DeviceId deviceId : devicesDetected) {
        DeviceId deviceId = new DeviceId();
        deviceId.setInstanceNumber(8);
        deviceId.setIpAddress("192.168.2.118");
            String deviceIpAddress = deviceId.getIpAddress();
            try {
                if (hasValue(deviceIpAddress)) {
                    ServicesSupportedCommand servicesSupportedCommand = new ServicesSupportedCommand();
                    servicesSupportedCommand.local(deviceId);
                    Thread.sleep(1000);
                } else {
                    log.trace("Device is missing IpAddress. Can not find detailed information. {}", deviceId.toString());
                }
            } catch (IOException e) {
                log.trace("Failed to send services supported command to ip address: {}. Reason: {}", deviceIpAddress, e.getMessage());
            } catch (InterruptedException e) {
                //ignore
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
    }

    public void findSensorAndPropertiesConfiguration () {

    }

    public void findPresentValueForSensors () {

    }
}
