package com.flicker.user.common.kafka.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovieInfo {
    private int userSeq; // movie인 경우 null
    private Integer movieSeq;
    private Integer reviewSeq;
    private Double rating;
    private String type; // "REVIEW" or "MOVIE"
    private String action; // "Create" or "Delete"
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
