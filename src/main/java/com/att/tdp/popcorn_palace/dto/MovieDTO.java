package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;
    
    @NotBlank(message = "Genre is required")
    @Size(min = 1, max = 100, message = "Genre must be between 1 and 100 characters")
    private String genre;
    
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 1000, message = "Duration cannot exceed 1000 minutes")
    private Integer duration;
    
    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", message = "Rating cannot exceed 10.0")
    private Double rating;
    
    @NotNull(message = "Release year is required")
    @Min(value = 1888, message = "Release year must be at least 1888")
    @Max(value = 2100, message = "Release year cannot exceed 2100")
    private Integer releaseYear;
}
