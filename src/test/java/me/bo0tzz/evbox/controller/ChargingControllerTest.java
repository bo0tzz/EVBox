package me.bo0tzz.evbox.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.bo0tzz.evbox.model.ChargingSession;
import me.bo0tzz.evbox.model.ChargingStation;
import me.bo0tzz.evbox.model.ChargingStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChargingControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper o;

    @Test
    public void testSubmitChargingSession() {

        ChargingStation station = new ChargingStation(12345);

        RequestEntity<ChargingStation> request = RequestEntity.post(URI.create("/chargingSession"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(station);

        ResponseEntity<ChargingSession> response = testRestTemplate.exchange(request, ChargingSession.class);

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

        RequestEntity<String> requestEntity = RequestEntity.post(URI.create("/chargingSession"))
                .contentType(MediaType.APPLICATION_JSON)
                .body("{}");
        ResponseEntity<String> stringResponseEntity = testRestTemplate.exchange(requestEntity, String.class);

        JsonNode response = o.readTree(stringResponseEntity.getBody());
        JsonNode errors = response.get("errors").get(0);
        assertEquals("stationId.invalid", errors.get("code").asText());

    }

}
