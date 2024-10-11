package com.flicker.logger.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.logger.dto.NewMovieDto;
import com.flicker.logger.dto.NewMovieUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class NewMovieUpdateService {

    private final WebClient webClient;

    public NewMovieUpdateService(WebClient.Builder webClientBuilder,
                                  @Value("${external.api.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public void newMovieUpdate(List<NewMovieUpdateRequest> newMovieDtos) {

        try {
            // 요청 데이터를 JSON으로 직렬화하여 출력
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(newMovieDtos);
            System.out.println("Request JSON: " + jsonString);
        } catch (Exception e) {
            log.error("Error in New Movie Update", e);
        }

        webClient.post()
                .uri("/movie_update")
                .body(Mono.just(newMovieDtos), List.class)
                .retrieve()
                .bodyToFlux(NewMovieUpdateRequest.class)
                .collectList()
                .block();
    }
}
