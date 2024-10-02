package com.flicker.movie.movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieSeqsRequest {
    List<String> movieTitle;
    List<Integer> movieYear;
}
