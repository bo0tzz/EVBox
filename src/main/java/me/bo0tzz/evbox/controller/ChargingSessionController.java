package me.bo0tzz.evbox.controller;

import me.bo0tzz.evbox.model.ChargingSession;
import me.bo0tzz.evbox.model.ChargingSessionSummary;
import me.bo0tzz.evbox.model.ChargingStation;
import me.bo0tzz.evbox.repository.ChargingSessionRepository;
import me.bo0tzz.evbox.validation.ChargingStationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@RestController
public class ChargingSessionController {

    private final ChargingStationValidator chargingStationValidator;
    private final ChargingSessionRepository repository;

    @Autowired
    public ChargingSessionController(ChargingStationValidator chargingStationValidator, ChargingSessionRepository repository) {
        this.chargingStationValidator = chargingStationValidator;
        this.repository = repository;
    }

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(chargingStationValidator);
    }

    /**
     * Submit a new charging session to a station.
     * @param station the station for which to start a session.
     * @return the started charging session.
     */
    @PostMapping(value = "/chargingSession", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ChargingSession> submitChargingSession(@RequestBody @Valid ChargingStation station) {
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
