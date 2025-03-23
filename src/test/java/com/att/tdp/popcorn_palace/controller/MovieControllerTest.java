package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the MovieController class.
 * Tests the REST endpoints for movie management functionality using Spring's WebMvcTest.
 */
@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;

    private ObjectMapper objectMapper;

    /**
     * Set up the test environment before each test.
     * Initializes the ObjectMapper.
     */
    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Tests the getAllMovies endpoint.
     * Verifies that the endpoint returns all movies with correct data.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetAllMovies() throws Exception {
        // Given
        MovieDTO movie1 = new MovieDTO(1L, "Test Movie 1", "Action", 120, 8.5, 2023);
        MovieDTO movie2 = new MovieDTO(2L, "Test Movie 2", "Comedy", 90, 7.5, 2022);
        List<MovieDTO> testMovies = Arrays.asList(movie1, movie2);

        // When
        when(movieService.getAllMovies()).thenReturn(testMovies);

        // Then
        mockMvc.perform(get("/movies/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Movie 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Test Movie 2"));
    }

    /**
     * Tests the addMovie endpoint.
     * Verifies that the endpoint successfully creates a new movie and returns it with an ID.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testAddMovie() throws Exception {
        // Given
        MovieDTO inputMovie = new MovieDTO(null, "New Movie", "Drama", 110, 8.0, 2024);
        MovieDTO savedMovie = new MovieDTO(3L, "New Movie", "Drama", 110, 8.0, 2024);

        // When
        when(movieService.addMovie(any(MovieDTO.class))).thenReturn(savedMovie);

        // Then
        String inputJson = objectMapper.writeValueAsString(inputMovie);
        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("New Movie"));
    }

    /**
     * Tests the updateMovie endpoint.
     * Verifies that the endpoint successfully updates an existing movie and returns the updated version.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testUpdateMovie() throws Exception {
        // Given
        MovieDTO inputMovie = new MovieDTO(null, "Updated Movie", "Drama", 110, 8.0, 2024);
        MovieDTO updatedMovie = new MovieDTO(4L, "Updated Movie", "Drama", 110, 8.0, 2024);

        // When
        when(movieService.updateMovie(eq("4"), any(MovieDTO.class))).thenReturn(updatedMovie);

        // Then
        String inputJson = objectMapper.writeValueAsString(inputMovie);
        mockMvc.perform(post("/movies/update/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.title").value("Updated Movie"));
    }

    /**
     * Tests the deleteMovie endpoint.
     * Verifies that the endpoint successfully processes a delete request and returns 204 No Content.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testDeleteMovie() throws Exception {
        // When
        doNothing().when(movieService).deleteMovie("Test Movie");

        // Then
        mockMvc.perform(delete("/movies/{movieTitle}", "Test Movie"))
                .andExpect(status().isNoContent());
    }
}