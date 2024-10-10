package com.flicker.movie.movie.dto;

import lombok.Data;

@Data
public class ActorUpdateRequest {
    private int movieSeq;
    private int actorSeq;
    private String actorName;
    private String role;
}
