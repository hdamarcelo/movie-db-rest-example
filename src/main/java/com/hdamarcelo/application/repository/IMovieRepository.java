package com.hdamarcelo.application.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.hdamarcelo.application.model.movies.Movie;

@ApplicationScoped
public interface IMovieRepository {

    public List<Movie> findAllMovies();

    public Movie findMovieById(String id);

    public String addMovie(Movie movie);

    public void deleteMovie(String id);
}
