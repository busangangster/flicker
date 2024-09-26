package com.flicker.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendByReviewReqeust {

    private int ownUserSeq;

    private List<RecommendReviewResponse> ownSentimentList;

    private List<RecommendSentimentReviewResponse> OtherSentimentList;

    public RecommendByReviewReqeust(List<RecommendReviewResponse> ownSentimentList, List<RecommendSentimentReviewResponse> OtherSentimentList) {
        this.ownSentimentList = ownSentimentList;
        this.OtherSentimentList = OtherSentimentList;
    }
}
