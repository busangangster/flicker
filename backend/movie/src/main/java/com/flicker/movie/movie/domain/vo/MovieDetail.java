package com.flicker.movie.movie.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class MovieDetail {
    private final String movieTitle;
    private final String director;
    private final String genre;
    private final String country;
    private final String moviePlot;
    private final String audienceRating;
    private final int movieYear;
    private final String runningTime;
    private final double movieRating;
    private final String moviePosterUrl;
    private final String movieTrailerUrl;
    private final String backgroundUrl;
}
