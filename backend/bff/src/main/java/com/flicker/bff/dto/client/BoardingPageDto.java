package com.flicker.bff.dto.client;

import lombok.Data;

import java.util.List;

@Data
public class BoardingPageDto {
    public boolean isLikeMovie;
    public MovieDto movie;
    public List<ReviewDto> reviewList;
    public List<RecommendMovie> recommendedMovieList;

}
