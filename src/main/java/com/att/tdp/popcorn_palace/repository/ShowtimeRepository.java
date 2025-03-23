package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Showtime entities.
 * Provides standard CRUD operations for showtimes.
 */
@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
}
