package com.flicker.user.review.dto;

import lombok.Data;

@Data
public class DeleteReviewReqDto {
    private Integer reviewSeq;
    private Integer userSeq;
}
