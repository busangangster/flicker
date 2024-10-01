package com.flicker.bff.dto.movie;

import lombok.Data;

import java.util.List;

@Data
public class UserMovieDetailResponse {
    private boolean bookMarkedMovie;
    private boolean unlikedMovie;
    private List<ReviewResponse> reviews;
    private List<Integer> unlikedMovies;
}
