package com.flicker.logger.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SentimentReviewEvent {

    private Long reviewSeq;
    private String content;
    private Double sentimentScore;
    private LocalDateTime timeStamp;
}
