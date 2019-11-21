package no.entra.bacnet.agent.rec;

import no.entra.bacnet.agent.RealEstateCoreMessage;

public interface Bacnet2RealEstateCore {
    RealEstateCoreMessage buildFromBacnetJson(String bacnetJson);
}
