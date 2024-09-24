package com.flicker.movie.movie.dto;

import com.flicker.movie.movie.domain.entity.Actor;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class ActorResponse {
    private String actorName; // 배우 이름

    private String role; // 영화에서 배우가 맡은 역할

    public ActorResponse(Actor actor) {
        this.actorName = actor.getActorName();
        this.role = actor.getRole();
    }
}
