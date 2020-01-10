package no.entra.bacnet.agent.devices;

import org.slf4j.Logger;

import static no.entra.bacnet.agent.json.JsonPathHelper.getStringFailsafeNull;
import static no.entra.bacnet.json.utils.StringUtils.hasValue;
import static org.slf4j.LoggerFactory.getLogger;

public class BacnetJsonDeviceIdParser {
    private static final Logger log = getLogger(BacnetJsonDeviceIdParser.class);

    /**
     * {
     *   "configurationRequest": {
     *     "observedAt": "2020-01-10T12:46:36.087069",
     *     "id": "TODO",
     *     "properties": {
     *       "DeviceInstanceRangeHighLimit": "1966",
     *       "DeviceInstanceRangeLowLimit": "1966"
     *                }*   },
     *   "sender": {
     *     "ip": "127.0.0.1",
     *     "port": "47808",
     *     "instanceNumber": 2002
     *   }    ,
     *   "service ": "WhoIs "
     * }
     * @param bacnetJson
     * @return
     */
    public static DeviceId parse(String bacnetJson) {
        DeviceId deviceId = new DeviceId();
        String ipAddressKey = "$.sender.ip";
        String ipAddress = getStringFailsafeNull(bacnetJson, ipAddressKey);
        deviceId.setIpAddress(ipAddress);
        String portKey = "$.sender.port";
        String port = getStringFailsafeNull(bacnetJson, portKey);
        deviceId.setPortNumber(port);
        String instanceNumberKey = "$.sender.instanceNumber";
        String instanceNumberString = getStringFailsafeNull(bacnetJson, instanceNumberKey);
        Integer instanceNumber = findIntegerInString(instanceNumberString);
        if (instanceNumber == null) {
            instanceNumber = lookForInstanceNumberProperty(bacnetJson);
        }
        if (instanceNumber != null) {
            deviceId.setInstanceNumber(instanceNumber);
        }
        return deviceId;
    }

    private static Integer findIntegerInString(String instanceNumberString) {
        Integer instanceNumber = null;
        if (hasValue(instanceNumberString)) {
            try {
                instanceNumber = Integer.valueOf(instanceNumberString);
            } catch (NumberFormatException e) {
                log.trace("Could not parse {} to integer.", instanceNumberString);
            }
        }
        return instanceNumber;
    }

    static Integer lookForInstanceNumberProperty(String bacnetJson) {
        Integer instanceNumber = null;
        String serviceKey = "$.service";
        String serviceValue = getStringFailsafeNull(bacnetJson,serviceKey);
        if (serviceValue != null && serviceValue.equals("IAm")) {
            String instanceNumberKey = "$.configurationRequest.properties.InstanceNumber";
            String instanceNumberString = getStringFailsafeNull(bacnetJson, instanceNumberKey);
            instanceNumber = findIntegerInString(instanceNumberString);
        }
        return instanceNumber;
    }
}
