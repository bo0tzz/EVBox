package me.bo0tzz.evbox.repository;

import me.bo0tzz.evbox.model.ChargingSessionSummary;

public interface ChargingSessionMetadataRepository {

    /**
     * Register the start of a charging session.
     */
    void registerSessionStart();

    /**
     * Register the stop of a charging session.
     */
    void registerSessionStop();

    /**
     * Summarize all charging sessions in the last minute.
     * @return a summary of charging sessions in the last minute.
     */
    ChargingSessionSummary getSummary();

}
