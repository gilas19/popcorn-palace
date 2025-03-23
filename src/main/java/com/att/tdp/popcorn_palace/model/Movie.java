package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a movie in the system.
 * Contains core information about a movie and its associated showtimes.
 */
@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    /**
     * Unique identifier for the movie.
     * Auto-incremented when a new movie is added to the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the movie.
     * Must be unique within the system to avoid duplicates.
     */
    @Column(nullable = false, unique = true)
    private String title;

    /**
     * Genre of the movie (e.g., "Action", "Comedy", "Drama").
     * Categorizes the movie for filtering and recommendation purposes.
     */
    @Column(nullable = false)
    private String genre;

    /**
     * Runtime duration of the movie in minutes.
     */
    @Column(nullable = false)
    private Integer duration;

    /**
     * Average rating of the movie on a scale of 0.0 to 10.0.
     */
    @Column(nullable = false)
    private Double rating;

    /**
     * Year when the movie was released.
     */
    @Column(nullable = false)
    private Integer releaseYear;

    /**
     * Collection of showtimes associated with this movie.
     * Establishes a one-to-many relationship with Showtime entity.
     * When a movie is deleted, all its showtimes will be deleted too (cascade).
     */
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes = new ArrayList<>();
}