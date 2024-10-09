package com.flicker.logger.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.logger.dto.NewMovieDto;
import com.flicker.logger.dto.WordCloudRequest;
import com.flicker.logger.dto.WordCloudResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class NewMovieUpdateService {

    private final WebClient webClient;

    public NewMovieUpdateService(WebClient.Builder webClientBuilder,
                                  @Value("${external.api.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public void updateWordCloud(List<NewMovieDto> newMovieDtos) {

        try {
            // 요청 데이터를 JSON으로 직렬화하여 출력
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(newMovieDtos);
            System.out.println("Request JSON: " + jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        webClient.post()
                .uri("/movie_update")
                .body(Mono.just(newMovieDtos), List.class)
                .retrieve()
                .bodyToFlux(NewMovieDto.class)
                .collectList()
                .block();
    }

}
