package com.flicker.bff.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendByContentRequest {
    private String movieTitle;
    private Integer year;
    private String actorName;
}
