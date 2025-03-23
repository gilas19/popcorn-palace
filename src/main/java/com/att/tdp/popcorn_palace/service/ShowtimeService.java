package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.exception.InvalidRequestException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public ShowtimeService(ShowtimeRepository showtimeRepository, MovieRepository movieRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
    }

    public ShowtimeDTO getShowtimeById(Long id) {
        if (id == null) {   
            throw new InvalidRequestException("Showtime ID cannot be null");
        }

        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", id));
        return convertToDTO(showtime);
    }

    @Transactional
    public ShowtimeDTO addShowtime(ShowtimeDTO showtimeDTO) {
        validateShowtimeDTO(showtimeDTO);

        // Check if movie exists
        movieRepository.findById(showtimeDTO.getMovieId())
            .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", showtimeDTO.getMovieId()));

        // Check if start time is before end time
        if (showtimeDTO.getStartTime().isAfter(showtimeDTO.getEndTime())) {
            throw new InvalidRequestException("Start time must be before end time");
        }

        Showtime newShowtime = convertToEntity(showtimeDTO);

        // Check for overlapping showtimes in the same theater
        checkForOverlappingShowtimes(newShowtime, null);

        Showtime savedShowtime = showtimeRepository.save(newShowtime);
        return convertToDTO(savedShowtime);
    }

    @Transactional
    public ShowtimeDTO updateShowtime(Long id, ShowtimeDTO showtimeDTO) {
        if (id == null) {
            throw new InvalidRequestException("Showtime ID cannot be null");
        }

        validateShowtimeDTO(showtimeDTO);

        // Check if showtime exists
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", id));

        // Check if movie exists
        Movie movie = movieRepository.findById(showtimeDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", showtimeDTO.getMovieId()));

        // Check if start time is before end time
        if (showtimeDTO.getStartTime().isAfter(showtimeDTO.getEndTime())) {
            throw new InvalidRequestException("Start time must be before end time");
        }

        showtime.setMovie(movie);
        showtime.setTheater(showtimeDTO.getTheater());
        showtime.setStartTime(showtimeDTO.getStartTime());
        showtime.setEndTime(showtimeDTO.getEndTime());
        showtime.setPrice(showtimeDTO.getPrice());

        // Check for overlapping showtimes in the same theater
        checkForOverlappingShowtimes(showtime, id);

        Showtime updatedShowtime = showtimeRepository.save(showtime);
        return convertToDTO(updatedShowtime);
    }

    @Transactional
    public void deleteShowtime(Long id) {
        if (id == null) {
            throw new InvalidRequestException("Showtime ID cannot be null");
        }

        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", id));

        showtimeRepository.delete(showtime);
    }

    // Helper methods

    private void validateShowtimeDTO(ShowtimeDTO showtimeDTO) {
        if (showtimeDTO == null) {
            throw new InvalidRequestException("Showtime data cannot be null");
        }

        if (showtimeDTO.getMovieId() == null) {
            throw new InvalidRequestException("Movie ID cannot be null");
        }

        if (showtimeDTO.getTheater() == null || showtimeDTO.getTheater().trim().isEmpty()) {
            throw new InvalidRequestException("Theater cannot be empty");
        }

        if (showtimeDTO.getStartTime() == null) {
            throw new InvalidRequestException("Start time cannot be null");
        }

        if (showtimeDTO.getEndTime() == null) {
            throw new InvalidRequestException("End time cannot be null");
        }

        if (showtimeDTO.getPrice() == null || showtimeDTO.getPrice() < 0) {
            throw new InvalidRequestException("Price must be a non-negative value");
        }

        // Check if start time is in the future
        ZonedDateTime now = ZonedDateTime.now();
        if (showtimeDTO.getStartTime().isBefore(now)) {
            throw new InvalidRequestException("Start time must be in the future");
        }
    }

    private void checkForOverlappingShowtimes(Showtime showtime, Long excludeId) {
        List<Showtime> existingShowtimes = showtimeRepository.findAll();

        for (Showtime existingShowtime : existingShowtimes) {
            // Skip comparing with itself (important for updates)
            if (excludeId != null && existingShowtime.getId().equals(excludeId)) {
                continue;
            }

            // Only check for overlaps in the same theater
            if (existingShowtime.getTheater().equals(showtime.getTheater())) {
                // Check for time overlap
                if (isTimeSlotOverlapping(
                        showtime.getStartTime(), showtime.getEndTime(),
                        existingShowtime.getStartTime(), existingShowtime.getEndTime())) {
                    throw new InvalidRequestException(
                            "Showtime overlaps with an existing showtime (ID: " + existingShowtime.getId() +
                                    ") in theater " + existingShowtime.getTheater());
                }
            }
        }
    }

    private boolean isTimeSlotOverlapping(ZonedDateTime start1, ZonedDateTime end1,
            ZonedDateTime start2, ZonedDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private ShowtimeDTO convertToDTO(Showtime showtime) {
        return new ShowtimeDTO(
                showtime.getId(),
                showtime.getMovie().getId(),
                showtime.getTheater(),
                showtime.getStartTime(),
                showtime.getEndTime(),
                showtime.getPrice());
    }

    private Showtime convertToEntity(ShowtimeDTO showtimeDTO) {
        Showtime showtime = new Showtime();

        Movie movie = movieRepository.findById(showtimeDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", showtimeDTO.getMovieId()));

        showtime.setMovie(movie);
        showtime.setTheater(showtimeDTO.getTheater());
        showtime.setStartTime(showtimeDTO.getStartTime());
        showtime.setEndTime(showtimeDTO.getEndTime());
        showtime.setPrice(showtimeDTO.getPrice());

        return showtime;
    }
}