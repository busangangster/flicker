package com.flicker.movie.movie.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flicker.movie.movie.domain.entity.MongoUserAction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserActionResponse {
    private int userSeq;

    private String keyword;

    private int movieYear;

    private String action; // "SEARCH" or "DETAIL"

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public UserActionResponse(MongoUserAction mongoUserAction) {
        this.userSeq = mongoUserAction.getUserSeq();
        this.keyword = mongoUserAction.getKeyword();
        this.action = mongoUserAction.getAction();
        this.timestamp = mongoUserAction.getTimestamp();
        this.movieYear = mongoUserAction.getMovieYear();
    }
}
