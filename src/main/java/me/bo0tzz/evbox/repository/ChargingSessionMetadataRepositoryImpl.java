package me.bo0tzz.evbox.repository;

import me.bo0tzz.evbox.model.ChargingSessionSummary;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@Repository
public class ChargingSessionMetadataRepositoryImpl implements ChargingSessionMetadataRepository {

    private final Deque<LocalDateTime> startedSessions;
    private final Deque<LocalDateTime> stoppedSessions;

    public ChargingSessionMetadataRepositoryImpl() {
        startedSessions = new ConcurrentLinkedDeque<>();
        stoppedSessions = new ConcurrentLinkedDeque<>();
    }

    private void prune() {
        synchronized (startedSessions) {
            pruneList(startedSessions);
        }
        synchronized (stoppedSessions) {
            pruneList(stoppedSessions);
        }
    }

    /**
     * This method will prune all elements older than 59 seconds from a LinkedList.
     * It expects chronological insertion order, where the head of the list is the oldest element.
     * @param list the list to prune from.
     */
    private void pruneList(Deque<LocalDateTime> list) {

        if (list.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime el = list.getFirst();
        Duration duration = Duration.between(el, now);

        while (duration.toMinutes() > 0) {
            list.removeFirst();
            if (list.isEmpty()) {
                return;
            }
            el = list.getFirst();
            duration = Duration.between(el, now);
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
