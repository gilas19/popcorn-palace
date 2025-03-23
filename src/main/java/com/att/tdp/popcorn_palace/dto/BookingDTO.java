package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

/**
 * Data Transfer Object for booking information.
 * Used for creating and modifying movie seat bookings.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    /**
     * ID of the showtime being booked.
     * Must not be null.
     */
    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    /**
     * Seat number to book.
     * Must be between 1 and 100, inclusive.
     */
    @NotNull(message = "Seat number is required")
    @Min(value = 1, message = "Seat number must be at least 1")
    @Max(value = 100, message = "Seat number cannot exceed 100")
    private Integer seatNumber;

    /**
     * UUID of the user making the booking.
     * Must be a valid UUID format.
     */
    @NotBlank(message = "User ID is required")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "User ID must be a valid UUID")
    private String userId;
}

/**
 * Data Transfer Object for booking response.
 * Contains the generated booking ID returned after successful booking creation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class BookingResponseDTO {
    /**
     * Unique identifier for the created booking.
     */
    private UUID bookingId;
}