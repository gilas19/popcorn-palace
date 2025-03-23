package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
public class MovieControllerTestasfdbjioa {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    private MovieDTO testMovie;
    private List<MovieDTO> testMovies;

    @BeforeEach
    void setUp() {
        testMovie = new MovieDTO();
        testMovie.setId(1L);
        testMovie.setTitle("Test Movie");
        testMovie.setGenre("Test Genre");
        testMovie.setDuration(120);
        testMovie.setRating(8.5);
        testMovie.setReleaseYear(2023);

        MovieDTO movie2 = new MovieDTO();
        movie2.setId(2L);
        movie2.setTitle("Another Movie");
        movie2.setGenre("Another Genre");
        movie2.setDuration(95);
        movie2.setRating(7.2);
        movie2.setReleaseYear(2022);

        testMovies = Arrays.asList(testMovie, movie2);
    }

    @Test
    void getAllMovies_ShouldReturnAllMovies() throws Exception {
        when(movieService.getAllMovies()).thenReturn(testMovies);

        mockMvc.perform(get("/movies/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Movie")))
                .andExpect(jsonPath("$[0].genre", is("Test Genre")))
                .andExpect(jsonPath("$[0].duration", is(120)))
                .andExpect(jsonPath("$[0].rating", is(8.5)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Another Movie")));

        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    void getMovieById_WhenMovieExists_ShouldReturnMovie() throws Exception {
        when(movieService.getMovieById(1L)).thenReturn(testMovie);

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Movie")))
                .andExpect(jsonPath("$.duration", is(120)))
                .andExpect(jsonPath("$.rating", is(8.5)));

        verify(movieService, times(1)).getMovieById(1L);
    }

    @Test
    void getMovieById_WhenMovieDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(movieService.getMovieById(99L)).thenThrow(new ResourceNotFoundException("Movie not found with id: 99"));

        mockMvc.perform(get("/movies/99"))
                .andExpect(status().isNotFound());

        verify(movieService, times(1)).getMovieById(99L);
    }

    @Test
    void addMovie_ShouldCreateAndReturnMovie() throws Exception {
        when(movieService.addMovie(any(MovieDTO.class))).thenReturn(testMovie);

        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMovie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Movie")))
                .andExpect(jsonPath("$.genre", is("Test Genre")))
                .andExpect(jsonPath("$.duration", is(120)))
                .andExpect(jsonPath("$.rating", is(8.5)));

        verify(movieService, times(1)).addMovie(any(MovieDTO.class));
    }

    @Test
    void updateMovie_WhenMovieExists_ShouldUpdateAndReturnMovie() throws Exception {
        when(movieService.updateMovie(eq("Test Movie"), any(MovieDTO.class))).thenReturn(testMovie);

        mockMvc.perform(post("/movies/update/Test Movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Movie")))
                .andExpect(jsonPath("$.duration", is(120)))
                .andExpect(jsonPath("$.rating", is(8.5)));

        verify(movieService, times(1)).updateMovie(eq("Test Movie"), any(MovieDTO.class));
    }

    @Test
    void updateMovie_WhenMovieDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(movieService.updateMovie(eq("Nonexistent Movie"), any(MovieDTO.class)))
                .thenThrow(new ResourceNotFoundException("Movie not found with title: Nonexistent Movie"));

        mockMvc.perform(post("/movies/update/Nonexistent Movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMovie)))
                .andExpect(status().isNotFound());

        verify(movieService, times(1)).updateMovie(eq("Nonexistent Movie"), any(MovieDTO.class));
    }

    @Test
    void deleteMovie_WhenMovieExists_ShouldReturnNoContent() throws Exception {
        doNothing().when(movieService).deleteMovie("Test Movie");

        mockMvc.perform(delete("/movies/Test Movie"))
                .andExpect(status().isNoContent());

        verify(movieService, times(1)).deleteMovie("Test Movie");
    }

    @Test
    void deleteMovie_WhenMovieDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Movie not found with title: Nonexistent Movie"))
                .when(movieService).deleteMovie("Nonexistent Movie");

        mockMvc.perform(delete("/movies/Nonexistent Movie"))
                .andExpect(status().isNotFound());

        verify(movieService, times(1)).deleteMovie("Nonexistent Movie");
    }

    @Test
    void searchMoviesByTitle_ShouldReturnMatchingMovies() throws Exception {
        when(movieService.searchMoviesByTitle("Test")).thenReturn(List.of(testMovie));

        mockMvc.perform(get("/movies/search?title=Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Movie")))
                .andExpect(jsonPath("$[0].duration", is(120)))
                .andExpect(jsonPath("$[0].rating", is(8.5)));

        verify(movieService, times(1)).searchMoviesByTitle("Test");
    }

    @Test
    void searchMoviesByTitle_WithBlankTitle_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/movies/search?title="))
                .andExpect(status().isBadRequest());

        verify(movieService, never()).searchMoviesByTitle(any());
    }

    @Test
    void addMovie_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        MovieDTO invalidMovie = new MovieDTO();
        invalidMovie.setTitle(""); // Empty title, should fail validation
        invalidMovie.setGenre("Action");
        invalidMovie.setDuration(120);
        invalidMovie.setRating(8.5);
        invalidMovie.setReleaseYear(2023);

        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidMovie)))
                .andExpect(status().isBadRequest());

        verify(movieService, never()).addMovie(any(MovieDTO.class));
    }

    @Test
    void addMovie_WithInvalidRating_ShouldReturnBadRequest() throws Exception {
        MovieDTO invalidMovie = new MovieDTO();
        invalidMovie.setTitle("Test Movie");
        invalidMovie.setGenre("Action");
        invalidMovie.setDuration(120);
        invalidMovie.setRating(11.5); // Rating exceeds maximum 10.0
        invalidMovie.setReleaseYear(2023);

        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidMovie)))
                .andExpect(status().isBadRequest());

        verify(movieService, never()).addMovie(any(MovieDTO.class));
    }
}