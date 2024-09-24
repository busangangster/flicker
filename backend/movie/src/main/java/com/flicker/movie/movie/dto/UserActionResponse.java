package com.flicker.movie.movie.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flicker.movie.movie.domain.entity.MongoUserAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class UserActionResponse {
    private int userSeq;

    private Integer movieSeq;

    private String keyword;

    private String action; // "SEARCH" or "DETAIL"

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public UserActionResponse(MongoUserAction mongoUserAction) {
        this.userSeq = mongoUserAction.getUserSeq();
        this.movieSeq = mongoUserAction.getMovieSeq();
        this.keyword = mongoUserAction.getKeyword();
        this.action = mongoUserAction.getAction();
        this.timestamp = mongoUserAction.getTimestamp();
    }
}
