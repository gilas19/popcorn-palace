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

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    public UUID bookTicket(BookingDTO bookingDTO) {
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

        Booking booking = new Booking();
        booking.setShowtime(showtime);
        booking.setSeatNumber(bookingDTO.getSeatNumber());
        booking.setUserId(bookingDTO.getUserId());

        Booking savedTicket = bookingRepository.save(booking);
        return savedTicket.getBookingId();
    }
}
