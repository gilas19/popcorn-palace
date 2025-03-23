package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a booking in the system.
 * Stores information about a user's reservation for a specific seat at a movie showtime.
 */
@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    /**
     * Unique identifier for the booking.
     * Automatically generated as a UUID when a booking is created.
     */
    @Id
    @GeneratedValue
    private UUID bookingId;

    /**
     * The showtime associated with this booking.
     * Establishes a many-to-one relationship with the Showtime entity.
     * Many bookings can be associated with a single showtime.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    /**
     * The seat number reserved in this booking.
     * Represents the specific seat within the theater.
     */
    @Column(nullable = false)
    private Integer seatNumber;

    /**
     * Identifier of the user who made the booking.
     * Stored as a string rather than an entity relationship.
     */
    @Column(nullable = false)
    private String userId;

    /**
     * Timestamp when the booking was created.
     * Automatically set to the current time when a booking is created.
     */
    @Column(nullable = false)
    private LocalDateTime bookingTime = LocalDateTime.now();
}