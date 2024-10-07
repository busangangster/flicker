package com.flicker.movie.movie.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class RecommendMovieListRequest {
    private List<MovieSeqListRequest> movieSeqListRequest;
    private List<Integer> unlikeMovieSeqList;
}
