package com.flicker.bff.dto.movie;

import lombok.Data;


@Data
public class MovieTitlesResponse {
    String movieTitles;

    public MovieTitlesResponse(MovieListResponse movieListResponses) {
        this.movieTitles = movieListResponses.getMovieTitle();
    }
}
