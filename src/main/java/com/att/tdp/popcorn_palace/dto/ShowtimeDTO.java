package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

/**
 * Data Transfer Object for movie showtime information.
 * Used for creating, retrieving, and modifying showtimes for movies.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeDTO {
    /**
     * Unique identifier for the showtime.
     * Generated automatically by the system when a showtime is created.
     */
    private Long id;

    /**
     * ID of the movie associated with this showtime.
     * References an existing movie in the system.
     */
    @NotNull(message = "Movie ID is required")
    private Long movieId;

    /**
     * Name or identifier of the theater where the movie will be shown.
     * Cannot be blank and must be within size constraints.
     */
    @NotBlank(message = "Theater is required")
    @Size(min = 1, max = 100, message = "Theater must be between 1 and 100 characters")
    private String theater;

    /**
     * Scheduled start time of the movie showing.
     * Must be a future date/time.
     */
    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private ZonedDateTime startTime;

    /**
     * Scheduled end time of the movie showing.
     * Must be a future date/time and logically must occur after the start time.
     */
    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private ZonedDateTime endTime;

    /**
     * Ticket price for this showtime in the system's currency.
     * Must be within the specified range.
     */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be at least 0.0")
    @DecimalMax(value = "1000.0", inclusive = true, message = "Price cannot exceed 1000.0")
    private Double price;
}