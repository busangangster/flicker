package com.flicker.bff.dto.movie;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
public class MovieDetailAndReviewAndRecommendResponse {
    private MovieDetailResponse movieDetailResponse;
    private boolean bookMarkedMovie;
    private boolean unlikedMovie;

    @Setter
    private List<ReviewResponse> reviews;

    @Setter
    private List<MovieListResponse> similarMovies;
}
