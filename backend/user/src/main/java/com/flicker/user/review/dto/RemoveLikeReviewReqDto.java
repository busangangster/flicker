package com.flicker.user.review.dto;

import lombok.Data;

@Data
public class RemoveLikeReviewReqDto {
    private Integer likeReviewSeq;
    private Integer userSeq;
}
