package com.flicker.bff.dto.client;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MovieDto {
    public int movieSeq;
    public MovieDetailDto movieDetail;
    public double movieRating;  // 영화 평점
    public List<ActorDto> actors;
}
