package com.flicker.bff.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.bff.common.module.exception.RestApiException;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.common.module.status.StatusCode;
import com.flicker.bff.dto.movie.MovieSeqListRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Util {
    private final WebClient.Builder webClientBuilder;

    private final ObjectMapper objectMapper;

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
            return webClient.get()
                    .uri(path)
                    .retrieve()
                    .bodyToMono(String.class)  // 응답을 문자열로 받음
                    .flatMap(response -> {
                        try {
                            // 응답을 ResponseDto로 변환
                            ResponseDto responseDto = objectMapper.readValue(response, ResponseDto.class);
                            return Mono.just(ResponseDto.response(StatusCode.of(responseDto.getHttpStatus(), responseDto.getServiceStatus(), responseDto.getMessage()), responseDto.getData()));
                        } catch (JsonProcessingException e) {
                            // 예외 발생 시 에러 메시지 출력 및 에러 처리
                            return Mono.error(new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "응답 변환 중 오류 발생: " + e.getMessage()));
                        }
                    })
                    .onErrorResume(e -> {
                        if (e instanceof RestApiException ex) {
                            return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                        } else {
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "WebClient GET 요청 중 오류 발생: " + e.getMessage()));
                        }
                    });
        } catch (Exception e) {
            throw new RestApiException(StatusCode.UNKNOW_ERROR, "WebClient GET 요청 중 알 수 없는 오류 발생: " + e.getMessage());
        }
    }

    // 공통으로 사용할 WebClient POST 요청 메서드 (비동기 처리)
    public <T> Mono<ResponseEntity<ResponseDto>> sendPostRequestAsync(String baseUrl, String path, T requestBody) {
        try {
            // WebClient 인스턴스 생성
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            return webClient.post()
                    .uri(path)
                    .body(requestBody != null ? BodyInserters.fromValue(requestBody) : BodyInserters.empty())
                    .retrieve()
                    .bodyToMono(String.class)  // 응답을 문자열로 받음
                    .flatMap(response -> {
                        try {
                            // 받은 응답을 ResponseDto로 변환
                            ResponseDto responseDto = objectMapper.readValue(response, ResponseDto.class);
                            return Mono.just(ResponseDto.response(StatusCode.of(responseDto.getHttpStatus(), responseDto.getServiceStatus(), responseDto.getMessage()), responseDto.getData()));
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
                            e.printStackTrace();
                            System.out.println("e.getMessage() = " + e.getMessage());
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "WebClient POST 요청 중 오류 발생: " + e.getMessage()));
                        }
                    });
        } catch (Exception e) {
            throw new RestApiException(StatusCode.UNKNOW_ERROR, "WebClient POST 요청 중 알 수 없는 오류 발생: " + e.getMessage());
        }
    }

    // 로그인 처리용 토큰 포함 응답 생성  액세스 토큰과 쿠키가 모두 오는 버전
    public <T> Mono<ResponseEntity<ResponseDto>> sendPostRequestAsyncWithToken(String baseUrl, String path, T requestBody) {
        try {
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            return webClient.post()
                    .uri(path)
                    .body(requestBody != null ? BodyInserters.fromValue(requestBody) : BodyInserters.empty())
                    .retrieve()
                    .toEntity(String.class)  // 응답을 ResponseEntity로 받음
                    .flatMap(responseEntity -> {
                        // 응답 헤더에서 JWT 토큰 읽기 (Authorization 헤더에 있다고 가정)
                        String jwtToken = responseEntity.getHeaders().getFirst("Authorization");

                        // 응답 헤더에서 Set-Cookie 읽기
                        String setCookieHeader = responseEntity.getHeaders().getFirst("Set-Cookie");
                        if (setCookieHeader != null) {
                            // Set-Cookie 헤더가 존재할 경우 처리
                        }

                        if (jwtToken != null) {
                            ResponseDto emptyResponseDto = new ResponseDto(
                                    null,  // data는 필요 없으므로 null
                                    "Success",  // 성공 메시지
                                    200,  // HTTP 상태 코드
                                    StatusCode.SUCCESS.getServiceStatus()  // 서비스 상태 코드
                            );

                            // ResponseEntity에 JWT 토큰을 포함하여 ResponseDto와 함께 반환
                            return Mono.just(ResponseEntity.ok()
                                    .header("Authorization", jwtToken)  // JWT 토큰을 헤더에 포함
                                    .header("Set-Cookie", setCookieHeader)  // Set-Cookie 헤더 추가
                                    .body(emptyResponseDto));  // 빈 ResponseDto 본문 포함
                        } else {
                            return Mono.error(new RestApiException(StatusCode.UNAUTHORIZED_REQUEST, "JWT 토큰이 응답에 포함되지 않았습니다."));
                        }
                    })
                    .onErrorResume(e -> {
                        if (e instanceof RestApiException ex) {
                            return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                        } else {
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "WebClient POST 요청 중 오류 발생: " + e.getMessage()));
                        }
                    });
        } catch (Exception e) {
            throw new RestApiException(StatusCode.UNKNOW_ERROR, "WebClient POST 요청 중 알 수 없는 오류 발생: " + e.getMessage());
        }
    }

    // 회원 수정 시 토큰 재발급 처리르 위한 메소드
    public <T> Mono<ResponseEntity<ResponseDto>> sendPutRequestAsyncWithToken(String baseUrl, String path, T requestBody) {
        try {
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            return webClient.put()
                    .uri(path)
                    .body(requestBody != null ? BodyInserters.fromValue(requestBody) : BodyInserters.empty())
                    .retrieve()
                    .toEntity(String.class)  // 응답을 ResponseEntity로 받음
                    .flatMap(responseEntity -> {
                        // 응답 헤더에서 JWT 토큰 읽기 (Authorization 헤더에 있다고 가정)
                        String jwtToken = responseEntity.getHeaders().getFirst("Authorization");

                        // 응답 헤더에서 Set-Cookie 읽기
                        String setCookieHeader = responseEntity.getHeaders().getFirst("Set-Cookie");
                        if (setCookieHeader != null) {
                            // Set-Cookie 헤더가 존재할 경우 처리
                        }

                        if (jwtToken != null) {
                            ResponseDto emptyResponseDto = new ResponseDto(
                                    null,  // data는 필요 없으므로 null
                                    "Success",  // 성공 메시지
                                    200,  // HTTP 상태 코드
                                    StatusCode.SUCCESS.getServiceStatus()  // 서비스 상태 코드
                            );

                            // ResponseEntity에 JWT 토큰을 포함하여 ResponseDto와 함께 반환
                            return Mono.just(ResponseEntity.ok()
                                    .header("Authorization", jwtToken)  // JWT 토큰을 헤더에 포함
                                    .header("Set-Cookie", setCookieHeader)  // Set-Cookie 헤더 추가
                                    .body(emptyResponseDto));  // 빈 ResponseDto 본문 포함
                        } else {
                            return Mono.error(new RestApiException(StatusCode.UNAUTHORIZED_REQUEST, "JWT 토큰이 응답에 포함되지 않았습니다."));
                        }
                    })
                    .onErrorResume(e -> {
                        if (e instanceof RestApiException ex) {
                            return Mono.just(ResponseDto.response(ex.getStatusCode(), ex.getData()));
                        } else {
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "WebClient PUT 요청 중 오류 발생: " + e.getMessage()));
                        }
                    });
        } catch (Exception e) {
            throw new RestApiException(StatusCode.UNKNOW_ERROR, "WebClient PUT 요청 중 알 수 없는 오류 발생: " + e.getMessage());
        }
    }

    // 토큰 재발급을 위한 메서드
    public <T> Mono<ResponseEntity<ResponseDto>> sendPostRequestAsyncForTokenGenerate(String baseUrl, String path, T requestBody, String token) {
        // Build the WebClient request with the refresh token in the cookie
        WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
        return webClient.post()
                .uri(path)
                .body(requestBody != null ? BodyInserters.fromValue(requestBody) : BodyInserters.empty())
                .cookie("refresh", token)
                .retrieve()
                .toEntity(String.class)
                .flatMap(response -> {
                    // 응답 헤더에서 JWT 토큰 읽기 (Authorization 헤더에 있다고 가정)
                    String jwtToken = response.getHeaders().getFirst("Authorization");

                    // 응답 헤더에서 Set-Cookie 읽기
                    String setCookieHeader = response.getHeaders().getFirst("Set-Cookie");
                    if (setCookieHeader != null) {
                        // Set-Cookie 헤더가 존재할 경우 처리
                    }

                    if (jwtToken != null) {
                        ResponseDto emptyResponseDto = new ResponseDto(
                                null,  // data는 필요 없으므로 null
                                "Success",  // 성공 메시지
                                200,  // HTTP 상태 코드
                                StatusCode.SUCCESS.getServiceStatus()  // 서비스 상태 코드
                        );

                        // ResponseEntity에 JWT 토큰을 포함하여 ResponseDto와 함께 반환
                        return Mono.just(ResponseEntity.ok()
                                .header("Authorization", jwtToken)  // JWT 토큰을 헤더에 포함
                                .header("Set-Cookie", setCookieHeader)  // Set-Cookie 헤더 추가
                                .body(emptyResponseDto));  // 빈 ResponseDto 본문 포함
                    } else {
                        return Mono.error(new RestApiException(StatusCode.UNAUTHORIZED_REQUEST, "JWT 토큰이 응답에 포함되지 않았습니다."));
                    }
                })
                .onErrorResume(e -> Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage())));
    }




    // 공통으로 사용할 WebClient PUT 요청 메서드 (비동기 처리)
    public <T> Mono<ResponseEntity<ResponseDto>> sendPutRequestAsync(String baseUrl, String path, T requestBody) {
        try {
            // WebClient 인스턴스 생성
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            return webClient.put()
                    .uri(path)
                    .body(requestBody != null ? BodyInserters.fromValue(requestBody) : BodyInserters.empty())
                    .retrieve()
                    .bodyToMono(String.class)  // 응답을 문자열로 받음
                    .flatMap(response -> {
                        try {
                            // 받은 응답을 ResponseDto로 변환
                            ResponseDto responseDto = objectMapper.readValue(response, ResponseDto.class);
                            return Mono.just(ResponseDto.response(StatusCode.of(responseDto.getHttpStatus(), responseDto.getServiceStatus(), responseDto.getMessage()), responseDto.getData()));
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
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "WebClient PUT 요청 중 오류 발생: " + e.getMessage()));
                        }
                    });
        } catch (Exception e) {
            throw new RestApiException(StatusCode.UNKNOW_ERROR, "WebClient PUT 요청 중 알 수 없는 오류 발생: " + e.getMessage());
        }
    }

    // 공통으로 사용할 WebClient DELETE 요청 메서드 (비동기 처리)
    public Mono<ResponseEntity<ResponseDto>> sendDeleteRequestAsync(String baseUrl, String path) {
        try {
            // WebClient 인스턴스 생성
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            return webClient.delete()
                    .uri(path)
                    .retrieve()
                    .bodyToMono(String.class)  // 응답을 문자열로 받음
                    .flatMap(response -> {
                        try {
                            // 받은 응답을 ResponseDto로 변환
                            ResponseDto responseDto = objectMapper.readValue(response, ResponseDto.class);
                            return Mono.just(ResponseDto.response(StatusCode.of(responseDto.getHttpStatus(), responseDto.getServiceStatus(), responseDto.getMessage()), responseDto.getData()));
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
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "WebClient DELETE 요청 중 오류 발생: " + e.getMessage()));
                        }
                    });
        } catch (Exception e) {
            throw new RestApiException(StatusCode.UNKNOW_ERROR, "WebClient DELETE 요청 중 알 수 없는 오류 발생: " + e.getMessage());
        }
    }


    public <T> Mono<List<MovieSeqListRequest>> sendPostRequestToRecommendServer(String baseUrl, String path, T requestBody) {
        WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

        return webClient.post()
                .uri(path)
                .body(requestBody != null ? BodyInserters.fromValue(requestBody) : BodyInserters.empty())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MovieSeqListRequest>>() {}) // 반환 타입 지정
                .onErrorResume(e -> Mono.error(e instanceof RestApiException ? e : new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "추천 서버 POST 요청 중 오류 발생: " + e.getMessage())));
    }

    public <T> Mono<ResponseEntity<ResponseDto>> sendGetWithRequestBodyRequestAsync(String baseUrl, String path, T requestBody) {
        try {
            // WebClient 인스턴스 생성
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            return webClient.method(HttpMethod.GET)
                    .uri(path)
                    .body(requestBody != null ? BodyInserters.fromValue(requestBody) : BodyInserters.empty())
                    .retrieve()
                    .bodyToMono(String.class)  // 응답을 문자열로 받음
                    .flatMap(response -> {
                        try {
                            // 받은 응답을 ResponseDto로 변환
                            ResponseDto responseDto = objectMapper.readValue(response, ResponseDto.class);
                            return Mono.just(ResponseDto.response(StatusCode.of(responseDto.getHttpStatus(), responseDto.getServiceStatus(), responseDto.getMessage()), responseDto.getData()));
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
                            e.printStackTrace();
                            System.out.println("e.getMessage() = " + e.getMessage());
                            return Mono.just(ResponseDto.response(StatusCode.INTERNAL_SERVER_ERROR, "WebClient POST 요청 중 오류 발생: " + e.getMessage()));
                        }
                    });
        } catch (Exception e) {
            throw new RestApiException(StatusCode.UNKNOW_ERROR, "WebClient POST 요청 중 알 수 없는 오류 발생: " + e.getMessage());
        }
    }


}
