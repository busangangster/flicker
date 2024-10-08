package com.flicker.bff.dto.movie;

import lombok.Data;

@Data
public class MovieListResponse {
    private int movieSeq;
    private String movieTitle;
    private String moviePosterUrl;
    private Integer movieYear;
    private String backgroundUrl;
    private Double movieRating;
    private String audienceRating;
    private String runningTime;
}
