package com.flicker.user.review.dto;

import lombok.Data;

@Data
public class RegisterReviewReqDto {
    private Integer userSeq;
    private Integer movieSeq;
    private Double reviewRating;
    private String content;
    private Boolean isSpoiler;
}
