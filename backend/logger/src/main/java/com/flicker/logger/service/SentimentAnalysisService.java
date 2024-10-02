package com.flicker.logger.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.logger.dto.SentimentResult;
import com.flicker.logger.dto.SentimentReviewEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SentimentAnalysisService {

    private final WebClient webClient;

    public SentimentAnalysisService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://222.107.238.75:8000").build();
    }

    public List<SentimentResult> batchAnalyze(List<SentimentReviewEvent> reviews) {

        try {
            // 요청 데이터를 JSON으로 직렬화하여 출력
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(reviews);
            System.out.println("Request JSON: " + jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return webClient.post()
                .uri("/sentiment_score")
                .body(Mono.just(reviews), List.class)
                .retrieve()
                .bodyToFlux(SentimentResult.class)
                .collectList()
                .block();
    }
}
