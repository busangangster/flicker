package com.flicker.bff.dto;

import lombok.Data;

import java.util.List;

@Data
public class TopReviewResponse {
    private List<ReviewResponse> topReviews;
}
