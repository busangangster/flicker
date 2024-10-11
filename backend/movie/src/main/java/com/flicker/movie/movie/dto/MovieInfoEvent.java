package com.flicker.movie.movie.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MovieInfoEvent {
    private int userSeq; // movie인 경우 null

    private Integer movieSeq; // review인 경우 null

    private Integer reviewSeq; // movie인 경우 null

    private Double rating; // movie인 경우 null

    private String type; // "REVIEW" or "MOVIE"

    private String action; // "Create" or "Delete"

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
