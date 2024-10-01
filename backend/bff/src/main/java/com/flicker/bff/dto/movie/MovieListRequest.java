package com.flicker.bff.dto.movie;

import lombok.Data;

import java.util.List;

@Data
public class MovieListRequest {
    List<Integer> movieSeqList;
    List<Integer> unlikeMovieSeqList;

    public MovieListRequest(List<Integer> movieSeqList, List<Integer> unlikeMovieSeqList) {
        this.movieSeqList = movieSeqList;
        this.unlikeMovieSeqList = unlikeMovieSeqList;
    }
}
