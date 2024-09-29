package com.flicker.user.common.kafka.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class WordCloudReviewSerializer implements Serializer<WordCloudReview> {

    private final ObjectMapper objectMapper;

    public WordCloudReviewSerializer() {
        this.objectMapper = new ObjectMapper();
        // Java 8 시간 처리를 위한 모듈 등록
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // 설정이 필요한 경우 여기에 추가
    }

    @Override
    public byte[] serialize(String topic, WordCloudReview data) {
        try {
            // Log 객체를 JSON으로 직렬화
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing Log object", e);
        }
    }

    @Override
    public void close() {
        // 자원 해제 필요시 구현
    }
}
