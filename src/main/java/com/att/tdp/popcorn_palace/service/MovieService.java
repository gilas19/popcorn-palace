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

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

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

    @Transactional
    public MovieDTO updateMovie(String title, MovieDTO movieDTO) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidRequestException("Movie title cannot be empty");
        }

        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "title", title));

        // If title is being changed, check if new title already exists (but not for the
        // current movie)
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

    @Transactional
    public void deleteMovie(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidRequestException("Movie title cannot be empty");
        }

        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "title", title));
        movieRepository.delete(movie);
    }

    private MovieDTO convertToDTO(Movie movie) {
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getGenre(),
                movie.getDuration(),
                movie.getRating(),
                movie.getReleaseYear());
    }

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
