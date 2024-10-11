package com.flicker.user.review.dto;

import lombok.Data;

@Data
public class CheckAlreadyReviewDto {
    private boolean alreadyReview;
    private ReviewDto reviewDto;
}
