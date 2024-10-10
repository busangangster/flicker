package com.flicker.movie.movie.domain.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
public class MongoMovie {
    private int movieSeq;
    private String movieTitle;
    private String moviePosterUrl;
    private int movieYear;
    private String backgroundUrl;
    private double movieRating;
    private String audienceRating;
    private String runningTime;
}
