package com.flicker.user.common.kafka.dto;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class SentimentReviewDeserializer implements Deserializer<SentimentReview> {

    private final ObjectMapper objectMapper;

    public SentimentReviewDeserializer() {
        this.objectMapper = new ObjectMapper();
        // Java 8 시간 처리를 위한 모듈 등록
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // 설정이 필요한 경우 여기에 추가
    }

    @Override
    public SentimentReview deserialize(String topic, byte[] data) {
        try {
            // 바이트 배열을 Log 객체로 역직렬화
            return objectMapper.readValue(data, SentimentReview.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing Log object", e);
        }
    }

    @Override
    public void close() {
        // 자원 해제 필요시 구현
    }
}

