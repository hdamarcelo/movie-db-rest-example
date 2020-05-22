package com.hdamarcelo.application.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.hdamarcelo.application.model.movies.AddMovieInboundDTO;
import com.hdamarcelo.application.model.movies.Movie;
import com.hdamarcelo.application.model.movies.MovieOutboundDTO;
import com.hdamarcelo.application.model.movies.UpdateMovieInboundDTO;
import com.hdamarcelo.application.repository.IMovieRepository;

@ApplicationScoped
public class MovieService {

    @Inject
    private IMovieRepository movieRepository;

    public List<MovieOutboundDTO> findAllMovies() {
        List<Movie> movies = movieRepository.findAllMovies();
        List<MovieOutboundDTO> response = new ArrayList<>();

        if (movies.isEmpty()) {
            return response;
        }

        for (Movie movie : movies) {
            response.add(
                    new MovieOutboundDTO(
                            movie.getId(),
                            movie.getTitle(),
                            movie.getDirector(),
                            movie.getGenre(),
                            movie.getLanguage(),
                            movie.getRating()));
        }

        return response;
    }

    public MovieOutboundDTO findMovieById(String id) {

        Movie movie = movieRepository.findMovieById(id);

        if (movie == null) {
            return null;
        }

        return new MovieOutboundDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getGenre(),
                movie.getLanguage(),
                movie.getRating());

    }

    public String addMovie(AddMovieInboundDTO movieDTO) throws MovieServiceException {
        if (findMovieById(movieDTO.getId()) != null) {
            throw new MovieServiceException("Cannot add movie, movie ID already exists");
        }
        return movieRepository.addMovie(
                new Movie(
                        movieDTO.getId(),
                        movieDTO.getTitle(),
                        movieDTO.getDirector(),
                        movieDTO.getGenre(),
                        movieDTO.getLanguage(),
                        movieDTO.getRating()));
    }

    public String updateMovie(String id, UpdateMovieInboundDTO updateMovieDTO) {
        return movieRepository.addMovie(
                new Movie(
                        id,
                        updateMovieDTO.getTitle(),
                        updateMovieDTO.getDirector(),
                        updateMovieDTO.getGenre(),
                        updateMovieDTO.getLanguage(),
                        updateMovieDTO.getRating()));
    }

    public void deleteMovie(String id) {
        movieRepository.deleteMovie(id);
    }
}
