package me.bo0tzz.evbox.repository;

import me.bo0tzz.evbox.model.ChargingSession;
import me.bo0tzz.evbox.model.ChargingStation;

import java.util.Optional;

public interface ChargingSessionRepository {

    /**
     * Generate a random, unused ID
     * @return an ID to be used for a session
     */
    int generateId();

    /**
     * Create a new charging session
     * @param chargingStation the charging station at which the session takes place
     * @return the newly created charging session
     */
    ChargingSession createNewSession(ChargingStation chargingStation);

    /**
     * Stop a charging session
     * @param id the id for the session to finalize
     * @return an Optional with the stopped charging session, or empty if the session does not exist
     */
    Optional<ChargingSession> stopChargingSession(Integer id);
}

