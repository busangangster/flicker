package com.flicker.movie.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendActorResponse {
    private String actorName;
    private String movieTitle;
}
