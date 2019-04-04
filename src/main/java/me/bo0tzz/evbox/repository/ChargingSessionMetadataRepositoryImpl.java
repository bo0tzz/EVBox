package me.bo0tzz.evbox.repository;

import me.bo0tzz.evbox.model.ChargingSessionSummary;
import me.bo0tzz.evbox.model.ChargingSessionTransition;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;

@Repository
public class ChargingSessionMetadataRepositoryImpl implements ChargingSessionMetadataRepository {

    private final LinkedList<ChargingSessionTransition> startedSessions;
    private final LinkedList<ChargingSessionTransition> stoppedSessions;

    public ChargingSessionMetadataRepositoryImpl() {
        startedSessions = new LinkedList<>();
        stoppedSessions = new LinkedList<>();
    }

    private void prune() {

        pruneList(startedSessions);
        pruneList(stoppedSessions);

    }

    /**
     * This method will prune all ChargingSessionTransition elements older than 59 seconds from a LinkedList.
     * @param list the list to trim from.
     */
    private void pruneList(LinkedList<ChargingSessionTransition> list) {

        if (list.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        ChargingSessionTransition stoppedEl = list.getFirst();
        Duration stoppedDuration = Duration.between(stoppedEl.getTimestamp(), now);

        while (stoppedDuration.toMinutes() > 0) {
            list.removeFirst();
            stoppedEl = list.getFirst();
            stoppedDuration = Duration.between(stoppedEl.getTimestamp(), now);
        }

    }

    @Override
    public void registerSessionStart() {
        prune();
        startedSessions.addLast(new ChargingSessionTransition());
    }

    @Override
    public void registerSessionStop() {
        prune();
        stoppedSessions.addLast(new ChargingSessionTransition());
    }

    @Override
    public ChargingSessionSummary getSummary() {
        prune();
        return ChargingSessionSummary.builder()
                .totalCount(totalCount())
                .startedCount(startedCount())
                .stoppedCount(stoppedCount())
                .build();
    }

    private int totalCount() {
        return startedCount() + stoppedCount();
    }

    private int startedCount() {
        return startedSessions.size();
    }

    private int stoppedCount() {
        return stoppedSessions.size();
    }

}
