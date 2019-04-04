package me.bo0tzz.evbox.controller;

import me.bo0tzz.evbox.model.ChargingSession;
import me.bo0tzz.evbox.model.ChargingSessionSummary;
import me.bo0tzz.evbox.model.ChargingStation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChargingSessionController {

    /**
     * Submit a new charging session to a station.
     * @param station the station for which to start a session.
     * @return the started charging session.
     */
    @PostMapping(value = "/chargingSession", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ChargingSession> submitChargingSession(@RequestBody ChargingStation station) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * Stop a charging session.
     * @param sessionId the charging session to stop.
     * @return the stopped charging session.
     */
    @PutMapping(value = "/chargingSession/{sessionId}", produces = "application/json")
    public ResponseEntity<ChargingSession> stopChargingSession(@PathVariable int sessionId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * Get a summary of recent charging sessions.
     * @return a summary of charging sessions for the last minute.
     */
    @GetMapping(value = "/chargingSessions", produces = "application/json")
    public ResponseEntity<ChargingSessionSummary> getChargingSessions() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

}
