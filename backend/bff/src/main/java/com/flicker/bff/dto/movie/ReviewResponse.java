package com.flicker.bff.dto.movie;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Integer reviewSeq;
    private Integer userSeq;
    private String nickname;
    private Integer movieSeq;
    private Double reviewRating;
    private String profilePhotoUrl;
    private String content;
    private LocalDateTime createdAt;
    private Boolean spoiler;
    private Integer likes;
    private boolean liked;
    private boolean top;
}
