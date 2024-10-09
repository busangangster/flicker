package com.flicker.logger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NewMovieDto {

    @JsonProperty("movie_seq")
    private int movieSeq;

    @JsonProperty("movie_title")
    private String movieTitle;

    private String genre;

    @JsonProperty("movie_year")
    private int movieYear;

    @JsonProperty("del_yn")
    private String delYn;

    private List<ActorDto> actors;

    @JsonProperty("new_check")
    private boolean newCheck;
}
