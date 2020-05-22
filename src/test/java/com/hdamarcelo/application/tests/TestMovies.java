package com.hdamarcelo.application.tests;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testcontainers.shaded.com.google.common.collect.Lists;

import com.hdamarcelo.application.model.movies.AddMovieInboundDTO;
import com.hdamarcelo.application.model.movies.Movie;
import com.hdamarcelo.application.model.movies.MovieOutboundDTO;
import com.hdamarcelo.application.model.movies.UpdateMovieInboundDTO;
import com.hdamarcelo.application.repository.MovieRepository;
import com.hdamarcelo.application.service.MovieService;
import com.hdamarcelo.application.service.MovieServiceException;

public class TestMovies {

    @Spy
    MovieRepository movieRepository = new MovieRepository();

    @InjectMocks
    MovieService movieService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShouldReturnAllMovies() {
        List<Movie> mockList = Arrays.asList(new Movie("1", "The Matrix"), new Movie("2", "Tropical Thunder"),
                new Movie("3", "The Other Guys"), new Movie("4", "John Wick"));
        when(movieRepository.findAllMovies()).thenReturn(mockList);
        List<MovieOutboundDTO> movies = movieService.findAllMovies();
        assertEquals(movies.size(), 4);
        assertThat(Lists.newArrayList(movies.stream().map(MovieOutboundDTO::getId).collect(Collectors.toList())),
                containsInAnyOrder("1", "2", "3", "4"));
    }

    @Test
    public void testShouldReturnEmptyMovieList() {
        List<Movie> mockList = new ArrayList<>();
        when(movieRepository.findAllMovies()).thenReturn(mockList);
        List<MovieOutboundDTO> movies = movieService.findAllMovies();
        assertThat(movies, is(empty()));
    }

    @Test
    public void testShouldAddMovie() throws MovieServiceException {
        List<MovieOutboundDTO> movieListBeforeAddition = movieService.findAllMovies();
        doCallRealMethod().when(movieRepository).addMovie(any(Movie.class));
        movieService.addMovie(new AddMovieInboundDTO(
                "999",
                "The Lord of The Rings",
                "Peter Jackson",
                "Action/Adventure",
                "English",
                100));
        List<MovieOutboundDTO> movieListAfterAddition = movieService.findAllMovies();

        assertEquals(movieListBeforeAddition.size() + 1, movieListAfterAddition.size());
    }

    @Test
    public void testShouldUpdateMovie() throws MovieServiceException {
        List<MovieOutboundDTO> movieListBeforeUpdate = movieService.findAllMovies();
        Movie mockMovie = new Movie(movieListBeforeUpdate.get(0).getId(),
                "The Lord of The Rings",
                "Peter Jackson",
                "Action/Adventure",
                "English",
                100);
        doCallRealMethod().when(movieRepository).addMovie((any(Movie.class)));
        movieService.updateMovie(mockMovie.getId(),
                (new UpdateMovieInboundDTO(
                        mockMovie.getTitle(),
                        mockMovie.getDirector(),
                        mockMovie.getGenre(),
                        mockMovie.getLanguage(),
                        mockMovie.getRating())));
        List<MovieOutboundDTO> movieListAfterUpdate = movieService.findAllMovies();
        assertEquals(mockMovie.getId(), movieListAfterUpdate.get(0).getId());
        assertEquals(mockMovie.getTitle(), movieListAfterUpdate.get(0).getTitle());
        assertEquals(mockMovie.getDirector(), movieListAfterUpdate.get(0).getDirector());
        assertEquals(mockMovie.getGenre(), movieListAfterUpdate.get(0).getGenre());
        assertEquals(mockMovie.getLanguage(), movieListAfterUpdate.get(0).getLanguage());
        assertEquals(mockMovie.getRating(), movieListAfterUpdate.get(0).getRating());
    }

    @Test
    public void testShouldDeleteMovie() {
        List<MovieOutboundDTO> movieListBeforeDeletion = movieService.findAllMovies();
        doCallRealMethod().when(movieRepository).deleteMovie(isA(String.class));
        movieService.deleteMovie(movieListBeforeDeletion.get(0).getId());
        List<MovieOutboundDTO> movieListAfterDeletion = movieService.findAllMovies();

        assertEquals(movieListBeforeDeletion.size() - 1, movieListAfterDeletion.size());
    }

    @Test(expected = MovieServiceException.class)
    public void testShouldRaiseExceptionForMovieWithSameID() throws MovieServiceException {
        when(movieRepository.findMovieById("1")).thenReturn(new Movie("1", "The Two Towers"));
        movieService.addMovie(new AddMovieInboundDTO(
                "1",
                "The Lord of The Rings",
                "Peter Jackson",
                "Action/Adventure",
                "English",
                100));
    }

}