package com.flicker.bff.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieCreateRequest implements MovieRequest, ActorRequest {

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
    private List<Actor> actorList;

}
