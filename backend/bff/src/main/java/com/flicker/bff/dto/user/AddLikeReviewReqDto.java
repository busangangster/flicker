package com.flicker.bff.dto.user;

import lombok.Data;

@Data
public class AddLikeReviewReqDto {
    private Integer reviewSeq;
    private Integer userSeq;
}
