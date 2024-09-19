package com.flicker.movie.movie.dto;

import lombok.Data;

@Data
public class MovieRatingUpdateRequest {
    private int movieSeq;
    private Double movieRating;
}
