package com.hdamarcelo.application.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

import com.hdamarcelo.application.model.movies.Movie;
/**
 * Mock of movie entity data
 */
@ApplicationScoped
public class MovieRepository implements IMovieRepository {
    private Map<String, Movie> movies = new ConcurrentHashMap<>();

    public MovieRepository() {
        movies.put("1", new Movie("1", "The Matrix"));
        movies.put("2", new Movie("2", "Tropical Thunder"));
        movies.put("3", new Movie("3", "The Other Guys"));
        movies.put("4", new Movie("4", "John Wick"));
    }

    public MovieRepository(Movie... movies) {
        for (int i = 0; i < movies.length; i++) {
            this.movies.put(String.valueOf(i), movies[i]);
        }
    }

    @Override
    public List<Movie> findAllMovies() {
        return new ArrayList<>(movies.values());
    }

    @Override
    public Movie findMovieById(String id) {
        return movies.get(id);
    }

    @Override
    public String addMovie(Movie movie) {
        movies.put(movie.getId(), movie);
        return movie.getId();
    }

    @Override
    public void deleteMovie(String id) {
        movies.remove(id);
    }

}
