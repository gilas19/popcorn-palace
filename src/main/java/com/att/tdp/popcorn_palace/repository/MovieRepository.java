package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for managing Movie entities.
 * Provides CRUD operations and custom query methods for movies.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Finds a movie by its title.
     * Since movie titles are unique in the system (as defined in the Movie entity),
     * this method returns at most one result.
     * 
     * @param title The title of the movie to find
     * @return An Optional containing the movie if found, or empty if not found
     */
    Optional<Movie> findByTitle(String title);
}