package me.bo0tzz.evbox.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.bo0tzz.evbox.model.ChargingSession;
import me.bo0tzz.evbox.model.ChargingStation;
import me.bo0tzz.evbox.model.ChargingStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ChargingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper o;

    @Test
    public void testSubmitChargingSession() throws Exception {

        ChargingStation station = new ChargingStation(12345);
        String json = o.writeValueAsString(station);

        MvcResult result = mockMvc.perform(post("/chargingSession")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ChargingSession session = o.readValue(response, ChargingSession.class);

        assertEquals("Station IDs not equal", station.getStationId(), session.getStationId().getStationId());
        assertEquals("Charging status not 'IN_PROGRESS'", ChargingStatus.IN_PROGRESS, session.getStatus());
        assertNotEquals("Session ID not set", 0, session.getId());
        assertNotNull("Session timestamp not set", session.getStartedAt());

    }

}
