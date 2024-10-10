package com.flicker.bff.dto.movie;

import lombok.Data;

@Data
public class RecommendActorResponse {
    private String actorName;

    private String movieTitle;
}
