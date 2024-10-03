package com.flicker.bff.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendMovieByActorResponse {

    private String actorName;

    private String movieTitle;

    private List<MovieListResponse> movieListResponses;
}
