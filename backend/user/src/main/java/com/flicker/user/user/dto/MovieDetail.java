package com.flicker.user.user.dto;

import com.flicker.user.review.dto.ReviewDto;
import lombok.Data;

import java.util.List;

@Data
public class MovieDetail {

    private boolean bookMarkedMovie;
    private boolean unlikedMovie;
    private ReviewDto reviews;
    private List<Integer> unlikedMovies;
}
