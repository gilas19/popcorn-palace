package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.BookingDTO;
import com.att.tdp.popcorn_palace.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for managing movie ticket bookings.
 * Handles HTTP requests related to booking operations.
 */
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    /**
     * Constructs the BookingController with required dependencies.
     * 
     * @param bookingService Service that handles business logic for bookings
     */
    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Creates a new ticket booking.
     * 
     * @param bookingDTO Data transfer object containing booking information (seats, showtime, etc.)
     * @return ResponseEntity containing the UUID of the newly created booking
     * 
     * @apiNote Returns HTTP 200 OK with booking ID on success
     * @throws com.att.tdp.popcorn_palace.exception.SeatUnavailableException if selected seats are already booked
     * @throws com.att.tdp.popcorn_palace.exception.InvalidShowtimeException if the requested showtime doesn't exist
     * 
     */
    @PostMapping
    public ResponseEntity<Map<String, UUID>> bookTicket(@RequestBody BookingDTO bookingDTO) {
        UUID bookingId = bookingService.bookTicket(bookingDTO);
        Map<String, UUID> response = new HashMap<>();
        response.put("bookingId", bookingId);
        return ResponseEntity.ok(response);
    }
}