package me.bo0tzz.evbox.repository;

import me.bo0tzz.evbox.model.ChargingSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Repository
public class ChargingSessionRepositoryImpl implements ChargingSessionRepository {

    private final Map<Integer, ChargingSession> sessions;
    private final Random random;

    public ChargingSessionRepositoryImpl(Random random) {
        this.sessions = new HashMap<>();
        this.random = random;
    }

    @Override
    public int generateId() {
        int id = random.nextInt();

        //Avoid duplicate IDs
        while (sessions.containsKey(id)) {
            id = random.nextInt();
        }
        return id;
    }

    @Override
    public ChargingSession save(ChargingSession entity) {
        sessions.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<ChargingSession> findById(Integer id) {
        return Optional.ofNullable(sessions.get(id));
    }

}
