package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.service.MovieService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing movie operations.
 * Provides endpoints for creating, reading, updating, and deleting movie
 * information.
 */
@RestController
@RequestMapping("/movies")
@Validated
public class MovieController {
    private final MovieService movieService;

    /**
     * Constructs a MovieController with the required dependencies.
     * 
     * @param movieService Service for handling movie-related business logic
     */
    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Retrieves all movies in the system.
     * 
     * @return ResponseEntity containing a list of all movie DTOs
     * 
     * @apiNote Returns HTTP 200 OK with an empty list if no movies exist
     */
    @GetMapping("/all")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<MovieDTO> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    /**
     * Adds a new movie to the system.
     * 
     * @param movieDTO Data transfer object containing the movie information
     * @return ResponseEntity containing the created movie DTO with generated ID
     * 
     * @apiNote Returns HTTP 201 CREATED on success
     * @throws jakarta.validation.ConstraintViolationException if the movie data fails validation
     */
    @PostMapping
    public ResponseEntity<MovieDTO> addMovie(@Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO createdMovie = movieService.addMovie(movieDTO);
        return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
    }

    /**
     * Updates an existing movie identified by its title.
     * 
     * @param movieTitle Title of the movie to update (URL path variable)
     * @param movieDTO   Data transfer object containing the updated movie
     *                   information
     * @return ResponseEntity containing the updated movie DTO
     * 
     * @apiNote Returns HTTP 200 OK on success
     * @throws ResourceNotFoundException                       if no movie exists with the specified title
     * @throws jakarta.validation.ConstraintViolationException if the update data fails validation
     */
    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<MovieDTO> updateMovie(
            @PathVariable @NotBlank(message = "Movie title cannot be blank") String movieTitle,
            @Valid @RequestBody MovieDTO movieDTO) {
        try {
            MovieDTO updatedMovie = movieService.updateMovie(movieTitle, movieDTO);
            return ResponseEntity.ok(updatedMovie);
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    /**
     * Deletes a movie identified by its title.
     * 
     * @param movieTitle Title of the movie to delete (URL path variable)
     * @return ResponseEntity with no content
     * 
     * @apiNote Returns HTTP 204 NO CONTENT on successful deletion
     * @throws ResourceNotFoundException if no movie exists with the specified title
     */
    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable @NotBlank(message = "Movie title cannot be blank") String movieTitle) {
        try {
            movieService.deleteMovie(movieTitle);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }
}