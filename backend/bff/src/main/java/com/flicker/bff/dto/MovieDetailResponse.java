package com.flicker.bff.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieDetailResponse {
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
    private double movieRating;
    private List<ActorResponse> actors;
}
