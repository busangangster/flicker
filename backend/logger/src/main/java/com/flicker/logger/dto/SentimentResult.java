package com.flicker.logger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SentimentResult {

    private Long reviewSeq;
    private Double sentimentScore;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime timeStamp = LocalDateTime.now();
}
