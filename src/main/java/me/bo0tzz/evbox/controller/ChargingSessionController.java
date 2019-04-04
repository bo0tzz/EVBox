package me.bo0tzz.evbox.controller;

import me.bo0tzz.evbox.model.ChargingSession;
import me.bo0tzz.evbox.model.ChargingSessionSummary;
import me.bo0tzz.evbox.model.ChargingStation;
import me.bo0tzz.evbox.repository.ChargingSessionMetadataRepository;
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
    private final ChargingSessionRepository sessionRepository;
    private final ChargingSessionMetadataRepository metadataRepository;

    @Autowired
    public ChargingSessionController(ChargingStationValidator chargingStationValidator, ChargingSessionRepository sessionRepository, ChargingSessionMetadataRepository metadataRepository) {
        this.chargingStationValidator = chargingStationValidator;
        this.sessionRepository = sessionRepository;
        this.metadataRepository = metadataRepository;
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

        ChargingSession session = ChargingSession.builder()
                .stationId(station)
                .startedAt(new Date())
                .id(sessionRepository.generateId())
                .build();

        ChargingSession saved = sessionRepository.save(session);
        metadataRepository.registerSessionStart();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(saved);
    }

    /**
     * Stop a charging session.
     * @param sessionId the charging session to stop.
     * @return the stopped charging session.
     */
    @PutMapping(value = "/chargingSession/{sessionId}", produces = "application/json")
    public ResponseEntity<ChargingSession> stopChargingSession(@PathVariable int sessionId) {

        Optional<ChargingSession> optionalSession = sessionRepository.findById(sessionId);

        if (optionalSession.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ChargingSession session = optionalSession.get();
        session.finishCharging();
        ChargingSession saved = sessionRepository.save(session);
        metadataRepository.registerSessionStop();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(saved);
    }

    /**
     * Get a summary of recent charging sessions.
     * @return a summary of charging sessions for the last minute.
     */
    @GetMapping(value = "/chargingSessions", produces = "application/json")
    public ResponseEntity<ChargingSessionSummary> getChargingSessions() {

        ChargingSessionSummary summary = metadataRepository.getSummary();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(summary);
    }

}
