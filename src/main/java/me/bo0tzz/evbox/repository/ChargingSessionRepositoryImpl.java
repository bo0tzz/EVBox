package me.bo0tzz.evbox.repository;

import me.bo0tzz.evbox.model.ChargingSession;
import me.bo0tzz.evbox.model.ChargingStation;
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
    public ChargingSession createNewSession(ChargingStation chargingStation) {

        ChargingSession.ChargingSessionBuilder builder = ChargingSession.builder()
                .stationId(chargingStation)
                .startedAt(new Date());

        synchronized (sessions) {
            ChargingSession session = builder.id(this.generateId()).build();
            sessions.put(session.getId(), session);
            return session;
        }

    }

    @Override
    public Optional<ChargingSession> stopChargingSession(Integer id) {
        synchronized (sessions) {
            Optional<ChargingSession> session = this.findById(id);
            return session.map(s -> {
                s.finishCharging();
                this.update(s);
                return s;
            });
        }
    }

    private ChargingSession update(ChargingSession entity) {
        sessions.put(entity.getId(), entity);
        return entity;
    }

    private Optional<ChargingSession> findById(Integer id) {
        return Optional.ofNullable(sessions.get(id));
    }

}
