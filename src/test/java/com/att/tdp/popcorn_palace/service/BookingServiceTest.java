// package com.att.tdp.popcorn_palace.service;

// import com.att.tdp.popcorn_palace.dto.BookingDTO;
// import com.att.tdp.popcorn_palace.exception.InvalidRequestException;
// import com.att.tdp.popcorn_palace.model.Movie;
// import com.att.tdp.popcorn_palace.model.Showtime;
// import com.att.tdp.popcorn_palace.model.Booking;
// import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
// import com.att.tdp.popcorn_palace.repository.BookingRepository;
// import jakarta.persistence.EntityNotFoundException;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.time.LocalDateTime;
// import java.time.ZonedDateTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// public class BookingServiceTest {

//     @Mock
//     private BookingRepository bookingRepository;

//     @Mock
//     private ShowtimeRepository showtimeRepository;

//     @InjectMocks
//     private BookingService bookingService;

//     private BookingDTO validBookingDTO;
//     private Showtime validShowtime;
//     private Movie movie;
//     private List<Booking> existingBookings;

//     @BeforeEach
//     void setUp() {
//         movie = new Movie();
//         movie.setId(1L);
//         movie.setTitle("Test Movie");
//         movie.setGenre("Action");
//         movie.setDuration(120);
//         movie.setRating(8.5);
//         movie.setReleaseYear(2023);

//         validShowtime = new Showtime();
//         validShowtime.setId(1L);
//         validShowtime.setMovie(movie);
//         validShowtime.setTheater("Theater 1");
//         validShowtime.setStartTime(ZonedDateTime.now().plusDays(1));
//         validShowtime.setEndTime(ZonedDateTime.now().plusDays(1).plusHours(2));
//         validShowtime.setPrice(10.0);

//         existingBookings = new ArrayList<>();

//         validBookingDTO = new BookingDTO();
//         validBookingDTO.setShowtimeId(1L);
//         validBookingDTO.setSeatNumber(5);
//         validBookingDTO.setUserId(UUID.randomUUID().toString());
//     }

//     @Test
//     void bookTicket_WhenShowtimeNotFound_ShouldThrowEntityNotFoundException() {

//         when(showtimeRepository.findById(validBookingDTO.getShowtimeId())).thenReturn(Optional.empty());

//         EntityNotFoundException exception = assertThrows(
//                 EntityNotFoundException.class,
//                 () -> bookingService.bookTicket(validBookingDTO));

//         assertTrue(exception.getMessage().contains(validBookingDTO.getShowtimeId().toString()));

//         verify(showtimeRepository).findById(validBookingDTO.getShowtimeId());
//         verify(bookingRepository, never()).save(any(Booking.class));
//     }

//     @Test
//     void bookTicket_WhenSeatAlreadyTaken_ShouldThrowInvalidRequestException() {
//         Booking existingBooking = new Booking();
//         existingBooking.setBookingId(UUID.randomUUID());
//         existingBooking.setShowtime(validShowtime);
//         existingBooking.setSeatNumber(validBookingDTO.getSeatNumber());
//         existingBooking.setUserId(UUID.randomUUID().toString());
//         existingBooking.setBookingTime(LocalDateTime.now());

//         existingBookings.add(existingBooking);
//         validShowtime.setTickets(existingBookings);

//         when(showtimeRepository.findById(validBookingDTO.getShowtimeId())).thenReturn(Optional.of(validShowtime));

//         InvalidRequestException exception = assertThrows(
//                 InvalidRequestException.class,
//                 () -> bookingService.bookTicket(validBookingDTO));

//         assertTrue(exception.getMessage().contains("Seat " + validBookingDTO.getSeatNumber() + " is already taken"));

//         verify(showtimeRepository).findById(validBookingDTO.getShowtimeId());
//         verify(bookingRepository, never()).save(any(Booking.class));
//     }
// }

package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.BookingDTO;
import com.att.tdp.popcorn_palace.exception.InvalidRequestException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.Booking;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @InjectMocks
    private BookingService bookingService;

    private BookingDTO validBookingDTO;
    private Showtime validShowtime;
    private Movie movie;
    private List<Booking> existingBookings;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setGenre("Action");
        movie.setDuration(120);
        movie.setRating(8.5);
        movie.setReleaseYear(2023);

        validShowtime = new Showtime();
        validShowtime.setId(1L);
        validShowtime.setMovie(movie);
        validShowtime.setTheater("Theater 1");
        validShowtime.setStartTime(ZonedDateTime.now().plusDays(1));
        validShowtime.setEndTime(ZonedDateTime.now().plusDays(1).plusHours(2));
        validShowtime.setPrice(10.0);

        existingBookings = new ArrayList<>();
        validShowtime.setTickets(existingBookings);

        validBookingDTO = new BookingDTO();
        validBookingDTO.setShowtimeId(1L);
        validBookingDTO.setSeatNumber(5);
        validBookingDTO.setUserId(UUID.randomUUID().toString());
    }

    @Test
    void bookTicket_WhenShowtimeNotFound_ShouldThrowEntityNotFoundException() {
        // When
        when(showtimeRepository.findById(validBookingDTO.getShowtimeId())).thenReturn(Optional.empty());

        // Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookingService.bookTicket(validBookingDTO));
        assertTrue(exception.getMessage().contains(validBookingDTO.getShowtimeId().toString()));

        // Verify
        verify(showtimeRepository).findById(validBookingDTO.getShowtimeId());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void bookTicket_WhenSeatAlreadyTaken_ShouldThrowInvalidRequestException() {
        // Given
        Booking existingBooking = new Booking();
        existingBooking.setBookingId(UUID.randomUUID());
        existingBooking.setShowtime(validShowtime);
        existingBooking.setSeatNumber(validBookingDTO.getSeatNumber());
        existingBooking.setUserId(UUID.randomUUID().toString());
        existingBooking.setBookingTime(LocalDateTime.now());
        existingBookings.add(existingBooking);

        // When
        when(showtimeRepository.findById(validBookingDTO.getShowtimeId())).thenReturn(Optional.of(validShowtime));

        // Then
        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> bookingService.bookTicket(validBookingDTO));

        assertTrue(exception.getMessage().contains("Seat " + validBookingDTO.getSeatNumber() + " is already taken"));

        // Verify
        verify(showtimeRepository).findById(validBookingDTO.getShowtimeId());
        verify(bookingRepository, never()).save(any(Booking.class));
    }
}