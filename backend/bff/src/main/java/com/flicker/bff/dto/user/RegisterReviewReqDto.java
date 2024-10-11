package com.flicker.bff.dto.user;

import lombok.Data;

@Data
public class RegisterReviewReqDto {
    private Integer userSeq;
    private Integer movieSeq;
    private Double reviewRating;
    private String content;
    private Boolean isSpoiler;
}
