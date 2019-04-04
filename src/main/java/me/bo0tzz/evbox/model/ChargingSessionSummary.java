package me.bo0tzz.evbox.model;

import lombok.Value;

@Value
public class ChargingSessionSummary {

    int totalCount;
    int startedCount;
    int stoppedCount;

}
