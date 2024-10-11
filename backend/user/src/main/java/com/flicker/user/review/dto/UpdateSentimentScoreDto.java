package com.flicker.user.review.dto;

import lombok.Data;

@Data
public class UpdateSentimentScoreDto {
    private Integer reviewSeq;
    private Double sentimentScore;
}
