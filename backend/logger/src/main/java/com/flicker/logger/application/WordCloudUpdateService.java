package com.flicker.logger.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.logger.dto.WordCloudRequest;
import com.flicker.logger.dto.WordCloudResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class WordCloudUpdateService {

    private final WebClient webClient;

    public WordCloudUpdateService(WebClient.Builder webClientBuilder,
                                    @Value("${external.api.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public void updateWordCloud(List<WordCloudRequest> wordCloudEvents) {

        try {
            // 요청 데이터를 JSON으로 직렬화하여 출력
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(wordCloudEvents);
            System.out.println("Request JSON: " + jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        webClient.post()
                .uri("/word_cloud")
                .body(Mono.just(wordCloudEvents), List.class)
                .retrieve()
                .bodyToFlux(WordCloudRequest.class)
                .collectList()
                .block();
    }

    public List<WordCloudResult> getWordCloudUpdate () {

        // FastAPI 서버로 POST 요청 후, 응답을 WordCloudResponse 객체로 매핑
        return webClient.post()
                .uri("/wordcloud_update")
                .retrieve()
                .bodyToFlux(WordCloudResult.class)  // 응답 데이터를 WordCloudResponse로 처리
                .collectList()
                .block();
    }

}
