package me.bo0tzz.evbox.repository;

import me.bo0tzz.evbox.model.ChargingSession;

import java.util.Optional;

public interface ChargingSessionRepository {

    /**
     * Generate a random, unused ID
     * @return an ID to be used for a session
     */
    int generateId();

    ChargingSession save(ChargingSession session);

    Optional<ChargingSession> findById(Integer id);

}

