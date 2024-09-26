package com.flicker.bff.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendMovieListRequest {
    List<Integer> movieSeqList;
    List<Integer> unlikeMovieSeqList;

    public RecommendMovieListRequest(List<Integer> movieSeqList, List<Integer> unlikeMovieSeqList) {
        this.movieSeqList = movieSeqList;
        this.unlikeMovieSeqList = unlikeMovieSeqList;
    }
}
