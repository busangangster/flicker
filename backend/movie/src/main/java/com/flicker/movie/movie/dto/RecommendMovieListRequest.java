package com.flicker.movie.movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendMovieListRequest {
    private List<Integer> movieSeqList;
    private List<Integer> unlikeMovieSeqList;
}
