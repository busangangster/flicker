package com.flicker.movie.movie.dto;


public interface MovieRequest {
    String getMovieTitle();
    String getDirector();
    String getGenre();
    String getCountry();
    String getMoviePlot();
    String getAudienceRating();
    int getMovieYear();
    String getRunningTime();
    String getMoviePosterUrl();
    String getTrailerUrl();
    String getBackgroundUrl();
}