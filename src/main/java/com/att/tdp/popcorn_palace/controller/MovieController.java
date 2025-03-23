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

@RestController
@RequestMapping("/movies")
@Validated
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<MovieDTO> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @PostMapping
    public ResponseEntity<MovieDTO> addMovie(@Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO createdMovie = movieService.addMovie(movieDTO);
        return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
    }

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
