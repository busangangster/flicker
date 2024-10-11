package com.flicker.logger.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NewMovieUpdateRequest {

    private int movieSeq;
    private String movieTitle;
    private String genre;
    private int movieYear;
    private List<ActorRequest> actors;
}
