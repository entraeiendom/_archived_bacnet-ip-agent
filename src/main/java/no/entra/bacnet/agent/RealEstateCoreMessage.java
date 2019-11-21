package no.entra.bacnet.agent;

public class RealEstateCoreMessage {

    private String backnetJson = null;

    public RealEstateCoreMessage() {
    }

    public RealEstateCoreMessage(String bacnetJson) {
        this.backnetJson = bacnetJson;
    }

    public String getBacknetJson() {
        return backnetJson;
    }

    public void setBacknetJson(String backnetJson) {
        this.backnetJson = backnetJson;
    }
}
