package com.flicker.bff.dto.movie;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class MovieListRequest {
    List<Integer> unlikeMovieSeqList;
    List<MovieSeqListRequest> movieSeqListRequest;

    public MovieListRequest(List<MovieSeqListRequest> movieSeqListRequest, List<Integer> unlikeMovieSeqList) {
        this.movieSeqListRequest = movieSeqListRequest;
        this.unlikeMovieSeqList = unlikeMovieSeqList;
    }
}
