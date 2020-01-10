package no.entra.bacnet.agent.devices;

import static no.entra.bacnet.agent.json.JsonPathHelper.getStringFailsafeNull;
import static no.entra.bacnet.json.utils.StringUtils.hasValue;

public class BacnetJsonDeviceIdParser {

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
        if (hasValue(instanceNumberString)) {
            Integer instanceNumber = Integer.valueOf(instanceNumberString);
            if (instanceNumber != null) {
                deviceId.setInstanceNumber(instanceNumber);
            }
        }
        return deviceId;
    }
}
