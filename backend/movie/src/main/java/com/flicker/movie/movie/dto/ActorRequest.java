package com.flicker.movie.movie.dto;

import lombok.Data;

import java.util.List;


public interface ActorRequest {
    // Actor 리스트를 가져오는 메서드 선언
    List<Actor> getActorList();

    @Data
    class Actor {
        private String actorName;
        private String role;
    }
}

