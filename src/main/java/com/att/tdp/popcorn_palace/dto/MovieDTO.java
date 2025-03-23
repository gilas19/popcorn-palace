package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for movie information.
 * Used for creating, retrieving, and modifying movie details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    /**
     * Unique identifier for the movie.
     * Generated automatically by the system when a movie is created.
     */
    private Long id;

    /**
     * Title of the movie.
     * Cannot be blank and must be within size constraints.
     */
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    /**
     * Genre of the movie (e.g., "Action", "Comedy", "Drama").
     * Cannot be blank and must be within size constraints.
     */
    @NotBlank(message = "Genre is required")
    @Size(min = 1, max = 100, message = "Genre must be between 1 and 100 characters")
    private String genre;

    /**
     * Runtime duration of the movie in minutes.
     * Must be a positive value within the specified range.
     */
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 1000, message = "Duration cannot exceed 1000 minutes")
    private Integer duration;

    /**
     * Average rating of the movie on a scale of 0.0 to 10.0.
     * Must be within the specified range.
     */
    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", message = "Rating cannot exceed 10.0")
    private Double rating;

    /**
     * Year when the movie was released.
     * Must be a valid year within the specified range.
     * The lower bound (1888) corresponds to the earliest known film.
     */
    @NotNull(message = "Release year is required")
    @Min(value = 1888, message = "Release year must be at least 1888")
    @Max(value = 2100, message = "Release year cannot exceed 2100")
    private Integer releaseYear;
}