package com.flicker.bff.dto.user;

import lombok.Data;

@Data
public class UserReviewReqDto {
    private Integer userSeq;
    private Integer myUserSeq;

    public UserReviewReqDto() {
    }

    public UserReviewReqDto(Integer userSeq, Integer myUserSeq) {
        this.myUserSeq = myUserSeq;
        this.userSeq = userSeq;
    }
}
