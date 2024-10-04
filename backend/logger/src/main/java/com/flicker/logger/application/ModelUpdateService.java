package com.flicker.logger.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.logger.dto.MovieReviewEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ModelUpdateService {

    private final WebClient webClient;

    public ModelUpdateService(WebClient.Builder webClientBuilder,
                              @Value("${external.api.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public void updateModel(List<MovieReviewEvent> reviews) {

        try {
            // 요청 데이터를 JSON으로 직렬화하여 출력
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(reviews);
            System.out.println("Request JSON: " + jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        webClient.post()
                .uri("/update_model")
                .body(Mono.just(reviews), List.class)
                .retrieve()
                .bodyToFlux(MovieReviewEvent.class)
                .collectList()
                .block();
    }
}
