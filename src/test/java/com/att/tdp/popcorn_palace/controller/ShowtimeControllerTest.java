package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the ShowtimeController class.
 * Tests the REST endpoints for showtime management functionality using Spring's WebMvcTest.
 */
@WebMvcTest(ShowtimeController.class)
public class ShowtimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShowtimeService showtimeService;

    @Autowired
    private ObjectMapper objectMapper;

    private ShowtimeDTO showtimeDTO;

    /**
     * Set up the test environment before each test.
     * Creates a sample showtime DTO with future dates.
     */
    @BeforeEach
    void setUp() {
        ZonedDateTime startTime = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endTime = startTime.plusHours(2);

        showtimeDTO = new ShowtimeDTO(1L, 1L, "Theater 1", startTime, endTime, 10.50);
    }

    /**
     * Tests the getShowtimeById endpoint with a valid ID.
     * Verifies that the endpoint returns the showtime with correct data.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getShowtimeById_ShouldReturnShowtime() throws Exception {
        // When
        when(showtimeService.getShowtimeById(1L)).thenReturn(showtimeDTO);

        // Then
        mockMvc.perform(get("/showtimes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.movieId").value(1))
                .andExpect(jsonPath("$.theater").value("Theater 1"))
                .andExpect(jsonPath("$.price").value(10.50));
    }

    /**
     * Tests the getShowtimeById endpoint with a non-existent ID.
     * Verifies that the endpoint returns a 404 Not Found status.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getShowtimeById_ShouldReturnNotFound() throws Exception {
        // When
        when(showtimeService.getShowtimeById(999L))
                .thenThrow(new ResourceNotFoundException("Showtime not found with ID: 999"));

        // Then
        mockMvc.perform(get("/showtimes/999"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the addShowtime endpoint with valid input.
     * Verifies that the endpoint successfully creates a new showtime and returns it with an ID.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void addShowtime_ShouldCreateAndReturnShowtime() throws Exception {
        // When
        when(showtimeService.addShowtime(any(ShowtimeDTO.class))).thenReturn(showtimeDTO);

        // Then
        mockMvc.perform(post("/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(showtimeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.movieId").value(1))
                .andExpect(jsonPath("$.theater").value("Theater 1"))
                .andExpect(jsonPath("$.price").value(10.50));
    }

    /**
     * Tests the updateShowtime endpoint with valid input.
     * Verifies that the endpoint successfully updates an existing showtime and returns the updated version.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void updateShowtime_ShouldUpdateAndReturnShowtime() throws Exception {
        // When
        when(showtimeService.updateShowtime(eq(1L), any(ShowtimeDTO.class))).thenReturn(showtimeDTO);

        // Then
        mockMvc.perform(post("/showtimes/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(showtimeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.movieId").value(1))
                .andExpect(jsonPath("$.theater").value("Theater 1"))
                .andExpect(jsonPath("$.price").value(10.50));
    }

    /**
     * Tests the updateShowtime endpoint with a non-existent ID.
     * Verifies that the endpoint returns a 404 Not Found status.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void updateShowtime_ShouldReturnNotFound() throws Exception {
        // When
        when(showtimeService.updateShowtime(eq(999L), any(ShowtimeDTO.class)))
                .thenThrow(new ResourceNotFoundException("Showtime not found with ID: 999"));

        // Then
        mockMvc.perform(post("/showtimes/update/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(showtimeDTO)))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the deleteShowtime endpoint with a valid ID.
     * Verifies that the endpoint successfully processes a delete request and returns 204 No Content.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void deleteShowtime_ShouldReturnNoContent() throws Exception {
        // When
        doNothing().when(showtimeService).deleteShowtime(1L);

        // Then
        mockMvc.perform(delete("/showtimes/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests the deleteShowtime endpoint with a non-existent ID.
     * Verifies that the endpoint returns a 404 Not Found status.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void deleteShowtime_ShouldReturnNotFound() throws Exception {
        // When
        doThrow(new ResourceNotFoundException("Showtime not found with ID: 999"))
                .when(showtimeService).deleteShowtime(999L);

        // Then
        mockMvc.perform(delete("/showtimes/999"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the addShowtime endpoint with invalid input.
     * Verifies that the endpoint returns a 400 Bad Request status when validation fails.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void addShowtime_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        // Create an invalid ShowtimeDTO (missing required fields)
        ShowtimeDTO invalidDTO = new ShowtimeDTO();
        invalidDTO.setId(1L);

        // Then
        mockMvc.perform(post("/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }
}