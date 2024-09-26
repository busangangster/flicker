package com.flicker.bff.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flicker.bff.common.module.exception.RestApiException;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.common.module.status.StatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
public class Util {
    private final WebClient.Builder webClientBuilder;

    // WebClient.Builder를 생성자 주입으로 받아옴
    public Util(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    // URI를 생성하는 메서드
    public String getUri(String path) {
        try {
            return UriComponentsBuilder.fromUriString(path)
                    .build()
                    .toUriString();
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "URI 생성에 실패했습니다.: " + e.getMessage());
        }
    }

    // 공통으로 사용할 WebClient GET 요청 메서드 (비동기 처리)
    public Mono<ResponseEntity<ResponseDto>> sendGetRequestAsync(String baseUrl, String path) {
        try {
            // WebClient 인스턴스 생성
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            // ObjectMapper 인스턴스 생성 및 JavaTimeModule 등록
            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 처리를 위한 모듈 등록
            return webClient.get()
                    .uri(path)
                    .retrieve()
                    .bodyToMono(String.class)  // 응답을 문자열로 받음
                    .flatMap(response -> {
                        // 받은 응답을 sysout으로 출력
                        try {
                            // 응답을 ResponseDto로 변환
                            ResponseDto responseDto = objectMapper.readValue(response, ResponseDto.class);
                            return Mono.just(ResponseEntity.ok(responseDto));
                        } catch (JsonProcessingException e) {
                            // 예외 발생 시 에러 메시지 출력 및 에러 처리
                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "응답 변환 중 오류 발생: " + e.getMessage()));
                        }
                    })
                    .onErrorResume(e -> {
                        if (e instanceof RestApiException ex) {
                            return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                        } else {
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "WebClient GET 요청 중 알 수 없는 오류 발생: " + e.getMessage()));
                        }
                    });
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "WebClient GET 요청 중 알 수 없는 오류 발생: " + e.getMessage());
        }
    }

    // 공통으로 사용할 WebClient POST 요청 메서드 (비동기 처리)
    public <T> Mono<ResponseEntity<ResponseDto>> sendPostRequestAsync(String baseUrl, String path, T requestBody) {
        try {
            // WebClient 인스턴스 생성
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            // ObjectMapper 인스턴스 생성 및 JavaTimeModule 등록
            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 처리를 위한 모듈 등록
            return webClient.post()
                    .uri(path)
                    .body(requestBody != null ? BodyInserters.fromValue(requestBody) : BodyInserters.empty())
                    .retrieve()
                    .bodyToMono(String.class)  // 응답을 문자열로 받음
                    .flatMap(response -> {
                        try {
                            // 받은 응답을 ResponseDto로 변환
                            ResponseDto responseDto = objectMapper.readValue(response, ResponseDto.class);
                            return Mono.just(ResponseEntity.ok(responseDto));
                        } catch (JsonProcessingException e) {
                            // JSON 변환 중 오류 발생 시 예외 처리
                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "응답 변환 중 오류 발생: " + e.getMessage()));
                        }
                    })
                    .onErrorResume(e -> {
                        // WebClient 오류 처리
                        if (e instanceof RestApiException ex) {
                            return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                        } else {
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "WebClient POST 요청 중 알 수 없는 오류 발생: " + e.getMessage()));
                        }
                    });
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "WebClient POST 요청 중 알 수 없는 오류 발생: " + e.getMessage());
        }
    }

    // 공통으로 사용할 WebClient PUT 요청 메서드 (비동기 처리)
    public <T> Mono<ResponseEntity<ResponseDto>> sendPutRequestAsync(String baseUrl, String path, T requestBody) {
        try {
            // WebClient 인스턴스 생성
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            // ObjectMapper 인스턴스 생성 및 JavaTimeModule 등록
            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 처리를 위한 모듈 등록
            return webClient.put()
                    .uri(path)
                    .body(requestBody != null ? BodyInserters.fromValue(requestBody) : BodyInserters.empty())
                    .retrieve()
                    .bodyToMono(String.class)  // 응답을 문자열로 받음
                    .flatMap(response -> {
                        try {
                            // 받은 응답을 ResponseDto로 변환
                            ResponseDto responseDto = objectMapper.readValue(response, ResponseDto.class);
                            return Mono.just(ResponseEntity.ok(responseDto));
                        } catch (JsonProcessingException e) {
                            // JSON 변환 중 오류 발생 시 예외 처리
                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "응답 변환 중 오류 발생: " + e.getMessage()));
                        }
                    })
                    .onErrorResume(e -> {
                        // WebClient 오류 처리
                        if (e instanceof RestApiException ex) {
                            return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                        } else {
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "WebClient PUT 요청 중 알 수 없는 오류 발생: " + e.getMessage()));
                        }
                    });
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "WebClient PUT 요청 중 알 수 없는 오류 발생: " + e.getMessage());
        }
    }

    // 공통으로 사용할 WebClient DELETE 요청 메서드 (비동기 처리)
    public Mono<ResponseEntity<ResponseDto>> sendDeleteRequestAsync(String baseUrl, String path) {
        try {
            // WebClient 인스턴스 생성
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            // ObjectMapper 인스턴스 생성 및 JavaTimeModule 등록
            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 처리를 위한 모듈 등록
            return webClient.delete()
                    .uri(path)
                    .retrieve()
                    .bodyToMono(String.class)  // 응답을 문자열로 받음
                    .flatMap(response -> {
                        try {
                            // 받은 응답을 ResponseDto로 변환
                            ResponseDto responseDto = objectMapper.readValue(response, ResponseDto.class);
                            return Mono.just(ResponseEntity.ok(responseDto));
                        } catch (JsonProcessingException e) {
                            // JSON 변환 중 오류 발생 시 예외 처리
                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "응답 변환 중 오류 발생: " + e.getMessage()));
                        }
                    })
                    .onErrorResume(e -> {
                        // WebClient 오류 처리
                        if (e instanceof RestApiException ex) {
                            return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                        } else {
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "WebClient DELETE 요청 중 알 수 없는 오류 발생: " + e.getMessage()));
                        }
                    });
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "WebClient DELETE 요청 중 알 수 없는 오류 발생: " + e.getMessage());
        }
    }

}
