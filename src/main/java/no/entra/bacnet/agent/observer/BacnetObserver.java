package no.entra.bacnet.agent.observer;

public interface BacnetObserver {

    void bacnetHexStringReceived(String hexString);
}
