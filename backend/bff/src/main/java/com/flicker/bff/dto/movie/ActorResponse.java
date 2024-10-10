package com.flicker.bff.dto.movie;

import lombok.Data;

@Data
public class ActorResponse {
    private int actorSeq; // 배우 시퀀스

    private String actorName; // 배우 이름

    private String role; // 영화에서 배우가 맡은 역할
}
