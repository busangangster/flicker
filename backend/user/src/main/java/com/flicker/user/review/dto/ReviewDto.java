package com.flicker.user.review.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class ReviewDto {
    private Integer reviewSeq;
    private Integer userSeq;
    private String nickname;
    private Integer movieSeq;
    private Double reviewRating;

    private String content;
    private LocalDateTime createdAt;
    private Boolean spoiler;
    private Integer likes;
    private boolean liked;
}
