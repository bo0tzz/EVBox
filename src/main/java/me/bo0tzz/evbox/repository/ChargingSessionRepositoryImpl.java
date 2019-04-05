package me.bo0tzz.evbox.repository;

import me.bo0tzz.evbox.model.ChargingSession;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChargingSessionRepositoryImpl implements ChargingSessionRepository {

    private final Map<Integer, ChargingSession> sessions;
    private final Random random;

    public ChargingSessionRepositoryImpl(Random random) {
        this.sessions = Collections.synchronizedMap(new HashMap<>());
        this.random = random;
    }

    @Override
    public int generateId() {
        int id = random.nextInt(Integer.MAX_VALUE);

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
