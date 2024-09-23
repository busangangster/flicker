package com.flicker.movie.movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class ActorAddRequest implements ActorRequest{
    private int movieSeq;
    private List<Actor> actorList;
}
