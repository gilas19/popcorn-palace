package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;
    
    @NotNull(message = "Seat number is required")
    @Min(value = 1, message = "Seat number must be at least 1")
    @Max(value = 100, message = "Seat number cannot exceed 100")
    private Integer seatNumber;
    
    @NotBlank(message = "User ID is required")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "User ID must be a valid UUID")
    private String userId;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class BookingResponseDTO {
    private UUID bookingId;
}
