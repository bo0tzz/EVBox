package me.bo0tzz.evbox.repository;

import me.bo0tzz.evbox.model.ChargingSessionSummary;

public interface ChargingSessionMetadataRepository {

    void registerSessionStart();
    void registerSessionStop();

    ChargingSessionSummary getSummary();

}
