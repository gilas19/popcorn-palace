package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a movie showtime in the system.
 * Contains information about when and where a movie will be shown, along with associated bookings.
 */
@Entity
@Table(name = "showtimes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Showtime {
    /**
     * Unique identifier for the showtime.
     * Auto-incremented when a new showtime is added to the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The movie associated with this showtime.
     * Establishes a many-to-one relationship with the Movie entity.
     * Many showtimes can be scheduled for a single movie.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    /**
     * Name or identifier of the theater where the movie will be shown.
     */
    @Column(nullable = false)
    private String theater;

    /**
     * Scheduled start time of the movie showing.
     * Stored with timezone information using ZonedDateTime.
     */
    @Column(nullable = false)
    private ZonedDateTime startTime;

    /**
     * Scheduled end time of the movie showing.
     * Calculated based on the start time and the movie's duration.
     */
    @Column(nullable = false)
    private ZonedDateTime endTime;

    /**
     * Ticket price for this particular showtime.
     */
    @Column(nullable = false)
    private Double price;

    /**
     * Collection of bookings/tickets associated with this showtime.
     * Establishes a one-to-many relationship with the Booking entity.
     * When a showtime is deleted, all its bookings will be deleted too (cascade).
     */
    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> tickets = new ArrayList<>();
}