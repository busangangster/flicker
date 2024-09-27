package com.flicker.bff.dto;

import lombok.Data;


@Data
public class MovieTitlesResponse {
    String movieTitles;

    public MovieTitlesResponse(MovieListResponse movieListResponses) {
        this.movieTitles = movieListResponses.getMovieTitle();
    }
}
