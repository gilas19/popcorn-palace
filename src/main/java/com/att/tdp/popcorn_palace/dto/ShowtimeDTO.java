package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeDTO {
    private Long id;
    
    @NotNull(message = "Movie ID is required")
    private Long movieId;
    
    @NotBlank(message = "Theater is required")
    @Size(min = 1, max = 100, message = "Theater must be between 1 and 100 characters")
    private String theater;
    
    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private ZonedDateTime startTime;
    
    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private ZonedDateTime endTime;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be at least 0.0")
    @DecimalMax(value = "1000.0", inclusive = true, message = "Price cannot exceed 1000.0")
    private Double price;
}
