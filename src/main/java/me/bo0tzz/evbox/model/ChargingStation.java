package me.bo0tzz.evbox.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Value;

@Value
public class ChargingStation {

    int stationId;

    @JsonCreator
    public ChargingStation(int stationId) {
        this.stationId = stationId;
    }

}
