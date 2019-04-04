package me.bo0tzz.evbox.repository;

import me.bo0tzz.evbox.model.ChargingSessionSummary;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;

@Repository
public class ChargingSessionMetadataRepositoryImpl implements ChargingSessionMetadataRepository {

    private final LinkedList<LocalDateTime> startedSessions;
    private final LinkedList<LocalDateTime> stoppedSessions;

    public ChargingSessionMetadataRepositoryImpl() {
        startedSessions = new LinkedList<>();
        stoppedSessions = new LinkedList<>();
    }

    private void prune() {
        pruneList(startedSessions);
        pruneList(stoppedSessions);
    }

    /**
     * This method will prune all elements older than 59 seconds from a LinkedList.
     * It expects chronological insertion order, where the head of the list is the oldest element.
     * @param list the list to prune from.
     */
    private void pruneList(LinkedList<LocalDateTime> list) {

        if (list.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime stoppedEl = list.getFirst();
        Duration stoppedDuration = Duration.between(stoppedEl, now);

        while (stoppedDuration.toMinutes() > 0) {
            list.removeFirst();
            stoppedEl = list.getFirst();
            stoppedDuration = Duration.between(stoppedEl, now);
        }

    }

    @Override
    public void registerSessionStart() {
        prune();
        startedSessions.addLast(LocalDateTime.now());
    }

    @Override
    public void registerSessionStop() {
        prune();
        stoppedSessions.addLast(LocalDateTime.now());
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
