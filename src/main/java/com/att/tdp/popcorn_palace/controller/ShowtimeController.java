package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing movie showtimes.
 * Provides endpoints for creating, retrieving, updating, and deleting showtimes.
 */
@RestController
@RequestMapping("/showtimes")
@Validated
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    /**
     * Constructs a ShowtimeController with the required dependencies.
     * 
     * @param showtimeService Service for handling showtime-related business logic
     */
    @Autowired
    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    /**
     * Retrieves a showtime by its ID.
     * 
     * @param showtimeId ID of the showtime to retrieve
     * @return ResponseEntity containing the requested showtime DTO
     * 
     * @apiNote Returns HTTP 200 OK on success
     * @throws ResourceNotFoundException                       if no showtime exists with the specified ID
     * @throws jakarta.validation.ConstraintViolationException if the showtime ID is not positive
     */
    @GetMapping("/{showtimeId}")
    public ResponseEntity<ShowtimeDTO> getShowtimeById(
            @PathVariable @Min(value = 1, message = "Showtime ID must be positive") Long showtimeId) {
        try {
            ShowtimeDTO showtime = showtimeService.getShowtimeById(showtimeId);
            return ResponseEntity.ok(showtime);
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    /**
     * Adds a new showtime to the system.
     * 
     * @param showtimeDTO Data transfer object containing the showtime information
     * @return ResponseEntity containing the created showtime DTO with generated ID
     * 
     * @apiNote Returns HTTP 201 CREATED on success
     * @throws jakarta.validation.ConstraintViolationException if the showtime data fails validation
     */
    @PostMapping
    public ResponseEntity<ShowtimeDTO> addShowtime(@Valid @RequestBody ShowtimeDTO showtimeDTO) {
        ShowtimeDTO createdShowtime = showtimeService.addShowtime(showtimeDTO);
        return new ResponseEntity<>(createdShowtime, HttpStatus.CREATED);
    }

    /**
     * Updates an existing showtime identified by its ID.
     * 
     * @param showtimeId  ID of the showtime to update
     * @param showtimeDTO Data transfer object containing the updated showtime information
     * @return ResponseEntity containing the updated showtime DTO
     * 
     * @apiNote Returns HTTP 200 OK on success
     * @throws ResourceNotFoundException                       if no showtime exists with the specified ID
     * @throws jakarta.validation.ConstraintViolationException if the showtime ID is not positive or if the update data
     *                                                         fails validation
     */
    @PostMapping("/update/{showtimeId}")
    public ResponseEntity<ShowtimeDTO> updateShowtime(
            @PathVariable @Min(value = 1, message = "Showtime ID must be positive") Long showtimeId,
            @Valid @RequestBody ShowtimeDTO showtimeDTO) {
        try {
            ShowtimeDTO updatedShowtime = showtimeService.updateShowtime(showtimeId, showtimeDTO);
            return ResponseEntity.ok(updatedShowtime);
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    /**
     * Deletes a showtime identified by its ID.
     * 
     * @param showtimeId ID of the showtime to delete
     * @return ResponseEntity with no content
     * 
     * @apiNote Returns HTTP 204 NO CONTENT on successful deletion
     * @throws ResourceNotFoundException                       if no showtime exists with the specified ID
     * @throws jakarta.validation.ConstraintViolationException if the showtime ID is not positive
     */
    @DeleteMapping("/{showtimeId}")
    public ResponseEntity<Void> deleteShowtime(
            @PathVariable @Min(value = 1, message = "Showtime ID must be positive") Long showtimeId) {
        try {
            showtimeService.deleteShowtime(showtimeId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }
}