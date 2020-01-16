package no.entra.bacnet.agent.devices;

import java.io.Serializable;

import static no.entra.bacnet.json.utils.StringUtils.hasValue;

public class DeviceId implements Serializable {

    private String id;
    private String ipAddress;
    private String portNumber;
    private String tfmTag; //aka TverfagligMerkesystem(TFM) in Norwegian RealEstate
    private Integer instanceNumber;
    private String macAdress;
    private String vendorId;
    private Integer gatewayInstanceNumber;
    private Integer gatewayDeviceId;

    public DeviceId() {
    }

    public DeviceId(String id) {
        this.id = id;
    }

    /**
     *
     * @param id
     * @param ipAddress
     * @param portNumber
     * @param tfmTag
     * @param instanceNumber
     */
    public DeviceId(String id, String ipAddress, String portNumber, String tfmTag, int instanceNumber) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.tfmTag = tfmTag;
        this.instanceNumber = instanceNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getTfmTag() {
        return tfmTag;
    }

    public void setTfmTag(String tfmTag) {
        this.tfmTag = tfmTag;
    }

    public Integer getInstanceNumber() {
        return instanceNumber;
    }

    public void setInstanceNumber(int instanceNumber) {
        this.instanceNumber = instanceNumber;
    }

    public String getMacAdress() {
        return macAdress;
    }

    public void setMacAdress(String macAdress) {
        this.macAdress = macAdress;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public void setInstanceNumber(Integer instanceNumber) {
        this.instanceNumber = instanceNumber;
    }

    public Integer getGatewayInstanceNumber() {
        return gatewayInstanceNumber;
    }

    public void setGatewayInstanceNumber(Integer gatewayInstanceNumber) {
        this.gatewayInstanceNumber = gatewayInstanceNumber;
    }

    public Integer getGatewayDeviceId() {
        return gatewayDeviceId;
    }

    public void setGatewayDeviceId(Integer gatewayDeviceId) {
        this.gatewayDeviceId = gatewayDeviceId;
    }

    public boolean isValid() {
        boolean isValid = false;
        if (hasValue(id)) {
            isValid = true;
        } else if (hasValue(tfmTag)) {
            isValid = true;
        } else if (hasValue(ipAddress)) {
            isValid = true;
        } else if (instanceNumber != null) {
            isValid = true;
        } else if (gatewayInstanceNumber != null && gatewayDeviceId != null) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public String toString() {
        return "DeviceId{" +
                "id='" + id + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", portNumber='" + portNumber + '\'' +
                ", tfmTag='" + tfmTag + '\'' +
                ", instanceNumber=" + instanceNumber +
                ", macAdress='" + macAdress + '\'' +
                ", vendorId='" + vendorId + '\'' +
                '}';
    }
}
