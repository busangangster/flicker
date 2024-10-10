package com.flicker.logger.application;

import com.flicker.logger.dto.NewMovieDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class NewMovieService {

    private final WebClient webClient;

    public NewMovieService(WebClient.Builder webClientBuilder,
                                  @Value("${newMovie.api.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public List<NewMovieDto> getNewMovies() {

        // FastAPI 서버로 POST 요청 후, 응답을 WordCloudResponse 객체로 매핑
        return webClient.post()
                .uri("/api/newMovie/list")
                .retrieve()
                .bodyToFlux(NewMovieDto.class)  // 응답 데이터를 WordCloudResponse로 처리
                .collectList()
                .block();
    }
}
