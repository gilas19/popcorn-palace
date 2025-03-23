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

@RestController
@RequestMapping("/showtimes")
@Validated
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    @Autowired
    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

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

    @PostMapping
    public ResponseEntity<ShowtimeDTO> addShowtime(@Valid @RequestBody ShowtimeDTO showtimeDTO) {
        ShowtimeDTO createdShowtime = showtimeService.addShowtime(showtimeDTO);
        return new ResponseEntity<>(createdShowtime, HttpStatus.CREATED);
    }

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
