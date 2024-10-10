package com.flicker.bff.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActorDto {
    private String actorName; // 배우 이름
    private String role; // 영화에서 배우가 맡은 역할
}
