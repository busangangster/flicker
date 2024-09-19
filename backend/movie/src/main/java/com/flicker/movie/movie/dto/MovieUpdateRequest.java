package com.flicker.movie.movie.dto;

import lombok.Data;

@Data
public class MovieUpdateRequest implements MovieRequest {
    private int movieSeq;
    private String movieTitle;
    private String director;
    private String genre;
    private String country;
    private String moviePlot;
    private String audienceRating;
    private int movieYear;
    private String runningTime;
    private String moviePosterUrl;
    private String trailerUrl;
    private String backgroundUrl;
}
