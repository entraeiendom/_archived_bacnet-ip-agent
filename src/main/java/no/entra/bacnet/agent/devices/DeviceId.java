package no.entra.bacnet.agent.devices;

import java.io.Serializable;

public class DeviceId implements Serializable {

    private String id;
    private String ipAddress;
    private String portNumber;
    private String tfmTag; //aka TverfagligMerkesystem(TFM) in Norwegian RealEstate
    private int instanceNumber;
    private String macAdress;
    private String vendorId;

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

    public int getInstanceNumber() {
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
