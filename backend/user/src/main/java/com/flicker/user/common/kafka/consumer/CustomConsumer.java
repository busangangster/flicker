package com.flicker.user.common.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.user.common.kafka.dto.SentimentResult;
import com.flicker.user.common.kafka.dto.SentimentReview;
import com.flicker.user.review.application.ReviewService;
import com.flicker.user.review.dto.UpdateSentimentScoreDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomConsumer {

    private final ObjectMapper objectMapper;
    private final ReviewService reviewService;

    @KafkaListener(topics = "sentiment-result")
    public void consume(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String payload) {
        try {
            // JSON 문자열을 Log 객체로 변환
            SentimentResult logMessage = objectMapper.readValue(payload, SentimentResult.class);

            System.out.println("sentiment-result logMessage = " + logMessage);

            UpdateSentimentScoreDto dto = new UpdateSentimentScoreDto();
            dto.setReviewSeq(logMessage.getReviewSeq());
            dto.setSentimentScore(logMessage.getSentimentScore());
            reviewService.updateSentimentScore(dto);

            System.out.println("dto = " + dto);

            log.info("CONSUME TOPIC : " + topic);
            log.info("CONSUME PAYLOAD : " + logMessage);
        } catch (Exception e) {
            log.error("Error processing message: ", e);
        }
    }

    
    //kafka 테스트용
    @KafkaListener(topics = "sentiment-review")
    public void sentimentReviewConsume(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String payload) {
        try {
            // JSON 문자열을 Log 객체로 변환
            SentimentReview logMessage = objectMapper.readValue(payload, SentimentReview.class);
            log.info("CONSUME TOPIC : " + topic);
            log.info("CONSUME PAYLOAD : " + logMessage);
        } catch (Exception e) {
            log.error("Error processing message: ", e);
        }
    }

}