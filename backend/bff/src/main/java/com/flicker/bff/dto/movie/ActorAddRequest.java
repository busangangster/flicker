package com.flicker.bff.dto.movie;

import lombok.Data;

import java.util.List;

@Data
public class ActorAddRequest implements ActorRequest{
    private int movieSeq;
    private List<Actor> actorList;
}
