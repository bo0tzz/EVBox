package me.bo0tzz.evbox.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Data
public class ChargingSession {

    /**
     * Unique identifier of the charging session
     */
    final int id;

    /**
     * The ID of the charging station where this session happened
     */
    final ChargingStation stationId;

    /**
     * The timestamp at which the charging session started
     */
    final Date startedAt;

    /**
     * The status of the charging session
     */
    @Setter(AccessLevel.NONE)
    ChargingStatus status = ChargingStatus.IN_PROGRESS;

    public void finishCharging() {
        this.status = ChargingStatus.FINISHED;
    }

}
