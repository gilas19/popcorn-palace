package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Booking;
import com.att.tdp.popcorn_palace.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Repository interface for managing Booking entities.
 * Provides CRUD operations and custom query methods for bookings.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    /**
     * Checks if a booking already exists for a specific seat at a showtime.
     * Used to prevent double-booking of seats.
     * 
     * @param showtime   The showtime to check
     * @param seatNumber The seat number to check
     * @return true if the seat is already booked for the showtime, false otherwise
     */
    boolean existsByShowtimeAndSeatNumber(Showtime showtime, Integer seatNumber);
}