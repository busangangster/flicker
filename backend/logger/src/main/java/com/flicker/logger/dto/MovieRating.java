package com.flicker.logger.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MovieRating {

    private Long movieSeq;
    private Double movieRating;
    private LocalDateTime dateTime;

    public MovieRating(Long movieSeq, Double movieRating) {
        this.movieSeq = movieSeq;
        this.movieRating = movieRating;
        this.dateTime = LocalDateTime.now();
    }
}
