package com.flicker.bff.dto.user;

import lombok.Data;

@Data
public class MovieReviewReqDto {
    private Integer movieSeq;
    private Integer userSeq;
    private Integer page;
    private Integer size;
    private String option;
}
