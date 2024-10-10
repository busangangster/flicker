package com.flicker.bff.dto.user;

import lombok.Data;

@Data
public class DeleteReviewReqDto {
    private Integer reviewSeq;
    private Integer userSeq;
}
