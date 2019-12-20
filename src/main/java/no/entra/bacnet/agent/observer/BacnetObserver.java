package no.entra.bacnet.agent.observer;

import java.net.InetAddress;

public interface BacnetObserver {

    void bacnetHexStringReceived(InetAddress sourceAddress, String hexString);
}
