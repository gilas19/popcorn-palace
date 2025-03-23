package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.exception.InvalidRequestException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class that handles business logic for movie operations.
 * Manages the creation, retrieval, updating, and deletion of movie records.
 */
@Service
public class MovieService {
    private final MovieRepository movieRepository;

    /**
     * Constructs a MovieService with the required dependencies.
     * 
     * @param movieRepository Repository for movie data access
     */
    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Retrieves all movies from the database.
     * 
     * @return List of MovieDTO objects representing all movies
     */
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Adds a new movie to the database.
     * 
     * @param movieDTO DTO containing the movie information
     * @return DTO representing the newly created movie
     * 
     * @throws InvalidRequestException if a movie with the same title already exists
     */
    @Transactional
    public MovieDTO addMovie(MovieDTO movieDTO) {
        // Check if movie with same title already exists
        if (movieDTO.getTitle() != null && movieRepository.findByTitle(movieDTO.getTitle()).isPresent()) {
            throw new InvalidRequestException("Movie with title '" + movieDTO.getTitle() + "' already exists");
        }

        Movie movie = convertToEntity(movieDTO);
        Movie savedMovie = movieRepository.save(movie);
        return convertToDTO(savedMovie);
    }

    /**
     * Updates an existing movie identified by its title.
     * 
     * @param title    Title of the movie to update
     * @param movieDTO DTO containing the updated movie information
     * @return DTO representing the updated movie
     * 
     * @throws InvalidRequestException   if the title is empty or if the new title already exists for another movie
     * @throws ResourceNotFoundException if no movie exists with the specified title
     */
    @Transactional
    public MovieDTO updateMovie(String title, MovieDTO movieDTO) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidRequestException("Movie title cannot be empty");
        }

        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "title", title));

        // If title is being changed, check if new title already exists (but not for the current movie)
        if (!title.equals(movieDTO.getTitle()) &&
                movieRepository.findByTitle(movieDTO.getTitle()).isPresent()) {
            throw new InvalidRequestException("Movie with title '" + movieDTO.getTitle() + "' already exists");
        }

        movie.setTitle(movieDTO.getTitle());
        movie.setGenre(movieDTO.getGenre());
        movie.setDuration(movieDTO.getDuration());
        movie.setRating(movieDTO.getRating());
        movie.setReleaseYear(movieDTO.getReleaseYear());

        Movie updatedMovie = movieRepository.save(movie);
        return convertToDTO(updatedMovie);
    }

    /**
     * Deletes a movie identified by its title.
     * 
     * @param title Title of the movie to delete
     * 
     * @throws InvalidRequestException   if the title is empty
     * @throws ResourceNotFoundException if no movie exists with the specified title
     */
    @Transactional
    public void deleteMovie(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidRequestException("Movie title cannot be empty");
        }

        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "title", title));

        movieRepository.delete(movie);
    }

    /**
     * Converts a Movie entity to a MovieDTO.
     * 
     * @param movie The Movie entity to convert
     * @return The equivalent MovieDTO
     */
    private MovieDTO convertToDTO(Movie movie) {
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getGenre(),
                movie.getDuration(),
                movie.getRating(),
                movie.getReleaseYear());
    }

    /**
     * Converts a MovieDTO to a Movie entity.
     * 
     * @param movieDTO The MovieDTO to convert
     * @return The equivalent Movie entity
     */
    private Movie convertToEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setGenre(movieDTO.getGenre());
        movie.setDuration(movieDTO.getDuration());
        movie.setRating(movieDTO.getRating());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        return movie;
    }
}