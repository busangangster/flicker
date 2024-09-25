package com.flicker.logger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MovieRating {

    private Long movieSeq;
    private Double movieRating;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;

    public MovieRating(Long movieSeq, Double movieRating) {
        this.movieSeq = movieSeq;
        this.movieRating = movieRating;
        this.timeStamp = LocalDateTime.now();
    }
}
