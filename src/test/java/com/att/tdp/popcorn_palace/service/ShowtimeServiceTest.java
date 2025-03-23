package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.exception.InvalidRequestException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShowtimeServiceTest {

    private static final Long MOVIE_ID = 1L;
    private static final Long EXISTING_SHOWTIME_ID = 2L;
    private static final Long UPDATE_SHOWTIME_ID = 3L;
    private static final Long ANOTHER_SHOWTIME_ID = 4L;
    private static final Long NONEXISTENT_SHOWTIME_ID = 99L;
    private static final String THEATER_NAME = "Theater 1";
    private static final double TICKET_PRICE = 10.0;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private ShowtimeService showtimeService;

    private Movie movie;
    private ShowtimeDTO validShowtimeDTO;
    private Showtime existingShowtime;
    private ZonedDateTime baseTime;

    @BeforeEach
    void setUp() {
        baseTime = ZonedDateTime.now().plusDays(1);

        movie = new Movie();
        movie.setId(MOVIE_ID);
        movie.setTitle("Test Movie");
        movie.setGenre("Action");
        movie.setDuration(120);
        movie.setRating(8.5);
        movie.setReleaseYear(2023);

        validShowtimeDTO = new ShowtimeDTO();
        validShowtimeDTO.setMovieId(MOVIE_ID);
        validShowtimeDTO.setTheater(THEATER_NAME);
        validShowtimeDTO.setStartTime(baseTime);
        validShowtimeDTO.setEndTime(baseTime.plusHours(2));
        validShowtimeDTO.setPrice(TICKET_PRICE);

        existingShowtime = new Showtime();
        existingShowtime.setId(EXISTING_SHOWTIME_ID);
        existingShowtime.setMovie(movie);
        existingShowtime.setTheater(THEATER_NAME);
        existingShowtime.setStartTime(baseTime.plusHours(3));
        existingShowtime.setEndTime(baseTime.plusHours(5));
        existingShowtime.setPrice(TICKET_PRICE);
    }

    @Test
    void addShowtime_WhenOverlap_ShouldThrowInvalidRequestException() {
        // When
        when(movieRepository.findById(validShowtimeDTO.getMovieId())).thenReturn(Optional.of(movie));

        validShowtimeDTO.setStartTime(baseTime.plusHours(4));
        validShowtimeDTO.setEndTime(baseTime.plusHours(6));

        when(showtimeRepository.findAll()).thenReturn(List.of(existingShowtime));

        // Then
        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> showtimeService.addShowtime(validShowtimeDTO));

        assertTrue(exception.getMessage().contains("overlaps"));

        // Verify
        verify(movieRepository, times(2)).findById(validShowtimeDTO.getMovieId());
        verify(showtimeRepository).findAll();
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    @Test
    void updateShowtime_WhenOverlap_ShouldThrowInvalidRequestException() {
        // Given
        Showtime showtimeToUpdate = new Showtime();
        showtimeToUpdate.setId(UPDATE_SHOWTIME_ID);
        showtimeToUpdate.setMovie(movie);
        showtimeToUpdate.setTheater(THEATER_NAME);
        showtimeToUpdate.setStartTime(baseTime.plusHours(6));
        showtimeToUpdate.setEndTime(baseTime.plusHours(8));
        showtimeToUpdate.setPrice(TICKET_PRICE);

        Showtime anotherExistingShowtime = new Showtime();
        anotherExistingShowtime.setId(ANOTHER_SHOWTIME_ID);
        anotherExistingShowtime.setMovie(movie);
        anotherExistingShowtime.setTheater(THEATER_NAME);
        anotherExistingShowtime.setStartTime(baseTime.plusHours(4));
        anotherExistingShowtime.setEndTime(baseTime.plusHours(6));
        anotherExistingShowtime.setPrice(TICKET_PRICE);

        // When
        when(showtimeRepository.findById(UPDATE_SHOWTIME_ID)).thenReturn(Optional.of(showtimeToUpdate));
        when(movieRepository.findById(validShowtimeDTO.getMovieId())).thenReturn(Optional.of(movie));

        validShowtimeDTO.setStartTime(baseTime.plusHours(5));
        validShowtimeDTO.setEndTime(baseTime.plusHours(7));

        when(showtimeRepository.findAll())
                .thenReturn(List.of(existingShowtime, showtimeToUpdate, anotherExistingShowtime));

        // Then
        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> showtimeService.updateShowtime(UPDATE_SHOWTIME_ID, validShowtimeDTO));

        assertTrue(exception.getMessage().contains("overlaps"));

        // Verify
        verify(showtimeRepository).findById(UPDATE_SHOWTIME_ID);
        verify(movieRepository).findById(validShowtimeDTO.getMovieId());
        verify(showtimeRepository).findAll();
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    @Test
    void updateShowtime_WhenShowtimeNotFound_ShouldThrowResourceNotFoundException() {
        // When
        when(showtimeRepository.findById(NONEXISTENT_SHOWTIME_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> showtimeService.updateShowtime(NONEXISTENT_SHOWTIME_ID, validShowtimeDTO));

        // Then
        assertTrue(exception.getMessage().contains("Showtime"));
        assertTrue(exception.getMessage().contains(NONEXISTENT_SHOWTIME_ID.toString()));

        // Verify
        verify(showtimeRepository).findById(NONEXISTENT_SHOWTIME_ID);
        verify(movieRepository, never()).findById(any());
        verify(showtimeRepository, never()).save(any());
    }
}