package com.hdamarcelo.application.model.movies;

import java.io.Serializable;

public class AddMovieInboundDTO implements Serializable {
    private static final long serialVersionUID = -8208299853265494025L;

    private String id;
    private String title;
    private String director;
    private String genre;
    private String language;
    private Integer rating;

    public AddMovieInboundDTO() {
    }

    public AddMovieInboundDTO(String id, String title, String director, String genre, String language, Integer rating) {
        super();
        this.id = id;
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.language = language;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

}
