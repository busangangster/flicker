package com.flicker.movie.movie.dto;

import lombok.Data;

@Data
public class MovieRatingEvent {
    private int movieSeq;

    private double movieRating;
}
