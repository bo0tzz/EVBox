package me.bo0tzz.evbox.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.bo0tzz.evbox.model.ChargingSession;
import me.bo0tzz.evbox.model.ChargingSessionSummary;
import me.bo0tzz.evbox.model.ChargingStation;
import me.bo0tzz.evbox.model.ChargingStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChargingControllerTest {

    private static final String BASE_PATH = "/chargingSession";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper o;

    @Test
    public void testSubmitChargingSession() {

        ChargingStation station = new ChargingStation(12345);

        RequestEntity<ChargingStation> request = RequestEntity.post(URI.create(BASE_PATH))
                .contentType(MediaType.APPLICATION_JSON)
                .body(station);

        ResponseEntity<ChargingSession> response = restTemplate.exchange(request, ChargingSession.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ChargingSession session = response.getBody();

        assertNotNull("Empty response", session);
        assertEquals("Station IDs not equal", station.getStationId(), session.getStationId().getStationId());
        assertEquals("Charging status not 'IN_PROGRESS'", ChargingStatus.IN_PROGRESS, session.getStatus());
        assertNotEquals("Session ID not set", 0, session.getId());
        assertNotNull("Session timestamp not set", session.getStartedAt());

    }

    @Test
    public void testSubmitInvalidChargingSession() throws IOException {

        RequestEntity<String> requestEntity = RequestEntity.post(URI.create(BASE_PATH))
                .contentType(MediaType.APPLICATION_JSON)
                .body("{}");
        ResponseEntity<String> stringResponseEntity = restTemplate.exchange(requestEntity, String.class);

        JsonNode response = o.readTree(stringResponseEntity.getBody());
        JsonNode errors = response.get("errors").get(0);
        assertEquals("stationId.invalid", errors.get("code").asText());

    }

    @Test
    public void testStopChargingSession() {

        //First, submit a charging session to be stopped.
        ChargingStation station = new ChargingStation(12345);
        RequestEntity<ChargingStation> createRequest = RequestEntity.post(URI.create(BASE_PATH))
                .contentType(MediaType.APPLICATION_JSON)
                .body(station);
        ResponseEntity<ChargingSession> createResponse = restTemplate.exchange(createRequest, ChargingSession.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        ChargingSession createdSession = createResponse.getBody();
        assertNotNull(createdSession);
        assertEquals(station, createdSession.getStationId());
        assertEquals(ChargingStatus.IN_PROGRESS, createdSession.getStatus());

        int sessionId = createdSession.getId();
        ResponseEntity<ChargingSession> response = restTemplate.exchange(
                BASE_PATH + "/{id}",
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                ChargingSession.class,
                sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ChargingSession updatedSession = response.getBody();

        assertNotNull(updatedSession);
        assertEquals(createdSession.getId(), updatedSession.getId());
        assertEquals(createdSession.getStartedAt(), updatedSession.getStartedAt());
        assertEquals(createdSession.getStationId(), updatedSession.getStationId());
        assertEquals(ChargingStatus.FINISHED, updatedSession.getStatus());

    }

    @Test
    public void testStopInvalidChargingSession() {

        int sessionId = 1234;
        ResponseEntity<ChargingSession> response = restTemplate.exchange(
                BASE_PATH + "/{id}",
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                ChargingSession.class,
                sessionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void testGetSessionSummary() {

        ResponseEntity<ChargingSessionSummary> response =
                restTemplate.getForEntity("/chargingSessions", ChargingSessionSummary.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ChargingSessionSummary chargingSessionSummary = response.getBody();
        assertNotNull(chargingSessionSummary);

        //TODO: expand this test with asserts about object contents.

    }

}
