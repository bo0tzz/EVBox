package me.bo0tzz.evbox.repository;

import me.bo0tzz.evbox.model.ChargingSession;

import java.util.Optional;

public interface ChargingSessionRepository {

    /**
     * Generate a random, unused ID
     * @return an ID to be used for a session
     */
    int generateId();

    /**
     * Save a ChargingSession
     * @param session the session to save
     * @return the saved session
     */
    ChargingSession save(ChargingSession session);

    /**
     * Find a ChargingSession by ID
     * @param id the ID by which to find the ChargingSession
     * @return an optional containing the ChargingSession, or empty if the session does not exist.
     */
    Optional<ChargingSession> findById(Integer id);

}

