package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.BookingDTO;
import com.att.tdp.popcorn_palace.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the BookingController class.
 * Tests the REST endpoints for ticket booking functionality.
 */
@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    /**
     * Set up the test environment before each test.
     * Initializes MockMvc and ObjectMapper.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
    }

    /**
     * Tests that the bookTicket endpoint successfully processes a valid booking request.
     * Verifies that the endpoint returns HTTP 200 OK with the booking ID.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void bookTicket_ValidInput_ReturnsBookingId() throws Exception {
        // Given
        BookingDTO bookingDTO = new BookingDTO(
                1L,
                42,
                "550e8400-e29b-41d4-a716-446655440000");

        // When
        UUID expectedBookingId = UUID.randomUUID();
        when(bookingService.bookTicket(any(BookingDTO.class))).thenReturn(expectedBookingId);

        // Then - perform request and verify response
        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(expectedBookingId.toString()));
    }
}