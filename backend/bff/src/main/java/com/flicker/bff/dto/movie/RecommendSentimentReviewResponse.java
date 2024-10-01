package com.flicker.bff.dto.movie;

import lombok.Data;

import java.util.List;

@Data
public class RecommendSentimentReviewResponse {

    private int userSeq;
    List<Sentiment> sentimentList;

    @Data
    static class Sentiment {
        private int movieSeq;
        private double sentimentScore;
    }

}
