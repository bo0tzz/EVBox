package me.bo0tzz.evbox.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChargingSessionSummary {

    int totalCount;
    int startedCount;
    int stoppedCount;

}
