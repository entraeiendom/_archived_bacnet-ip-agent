package no.entra.bacnet.agent;

/*
@Deprecated use no.entra.bacnet.rec.RealEstateCore
 */
public class RealEstateCoreMessage {

    private String backnetJson = null;
    private String hexString = null;

    public RealEstateCoreMessage() {
    }

    public RealEstateCoreMessage(String hexString) {
        this.hexString = hexString;
    }

    public String getBacknetJson() {
        return backnetJson;
    }

    public void setBacknetJson(String backnetJson) {
        this.backnetJson = backnetJson;
    }

    public String getHexString() {
        return hexString;
    }

    public void setHexString(String hexString) {
        this.hexString = hexString;
    }
}
