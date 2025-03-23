package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.BookingDTO;
import com.att.tdp.popcorn_palace.exception.InvalidRequestException;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.Booking;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * Service class that handles business logic for ticket booking operations.
 * Manages the creation of bookings and validation of booking requests.
 */
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;

    /**
     * Constructs a BookingService with the required dependencies.
     * 
     * @param bookingRepository  Repository for booking data access
     * @param showtimeRepository Repository for showtime data access
     */
    @Autowired
    public BookingService(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    /**
     * Books a ticket for a specific showtime and seat.
     * 
     * @param bookingDTO DTO containing booking information
     * @return UUID of the created booking
     * 
     * @throws EntityNotFoundException if the specified showtime doesn't exist
     * @throws InvalidRequestException if the requested seat is already taken
     */
    public UUID bookTicket(BookingDTO bookingDTO) {
        // Retrieve the showtime or throw exception if not found
        Showtime showtime = showtimeRepository.findById(bookingDTO.getShowtimeId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Showtime not found with id: " + bookingDTO.getShowtimeId()));

        // Check if the seat is already taken
        boolean isSeatTaken = showtime.getTickets().stream()
                .anyMatch(ticket -> ticket.getSeatNumber().equals(bookingDTO.getSeatNumber()));

        if (isSeatTaken) {
            throw new InvalidRequestException(
                    "Seat " + bookingDTO.getSeatNumber() + " is already taken for this showtime");
        }

        // Create and save the booking
        Booking booking = new Booking();
        booking.setShowtime(showtime);
        booking.setSeatNumber(bookingDTO.getSeatNumber());
        booking.setUserId(bookingDTO.getUserId());

        Booking savedTicket = bookingRepository.save(booking);
        return savedTicket.getBookingId();
    }
}