package com.flicker.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendMovieResponse {

    private String nickName;

    private List<MovieResponse> movies;

}
