package com.flicker.bff.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendByReviewRequest {

    private int ownUserSeq;

    private List<RecommendReviewResponse> ownSentimentList;

    private List<RecommendSentimentReviewResponse> OtherSentimentList;

    public RecommendByReviewRequest(List<RecommendReviewResponse> ownSentimentList, List<RecommendSentimentReviewResponse> OtherSentimentList) {
        this.ownSentimentList = ownSentimentList;
        this.OtherSentimentList = OtherSentimentList;
    }
}
