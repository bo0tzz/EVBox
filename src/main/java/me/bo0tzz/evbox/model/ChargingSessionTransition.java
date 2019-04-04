package me.bo0tzz.evbox.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ChargingSessionTransition {

    LocalDateTime timestamp = LocalDateTime.now();

}
