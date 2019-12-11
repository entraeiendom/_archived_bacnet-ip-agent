package no.entra.bacnet.agent.rec;


import no.entra.bacnet.rec.RealEstateCore;

public interface Bacnet2RealEstateCore {
    RealEstateCore buildFromBacnetJson(String bacnetJson);
}
