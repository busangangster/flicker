package com.flicker.user.review.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieReviewRatingCount {
    List<ReviewRatingCountDto> reviewRatingCount;
    Integer totalCnt;
}
