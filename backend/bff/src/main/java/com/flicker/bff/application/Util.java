package com.flicker.bff.application;

import com.flicker.bff.common.module.exception.RestApiException;
import com.flicker.bff.common.module.response.ResponseDto;
import com.flicker.bff.common.module.status.StatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

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
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "URI 생성에 실패했습니다.");
        }
    }

    // 공통으로 사용할 WebClient GET 요청 메서드 (동기 처리)
    public ResponseEntity<ResponseDto> sendGetRequest(String baseUrl, String path) {
        try {
            // baseUrl과 path를 결합하여 요청 URI를 구성하고 WebClient를 생성
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            // WebClient를 사용하여 GET 요청을 보내고 응답을 동기적으로 처리
            return webClient.post()  // HTTP GET 요청을 전송
                    .uri(path)  // 요청할 경로를 설정 (path는 호출 시 전달된 값)
                    .retrieve()  // 응답을 받아오기 시작 (응답을 받으면 이후 처리)
                    .toEntity(ResponseDto.class)  // 응답을 ResponseDto로 변환하여 ResponseEntity로 감싸서 반환
                    .block();  // 동기 처리: 이 메서드를 호출한 쓰레드는 응답을 받을 때까지 대기
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "WebClient GET 요청 중 오류 발생");
        }
    }

    // 공통으로 사용할 WebClient POST 요청 메서드 (동기 처리)
    public <T> ResponseEntity<ResponseDto> sendPostRequest(String baseUrl, String path, T requestBody) {
        try {
            // baseUrl과 path를 결합하여 요청 URI를 구성하고 WebClient를 생성
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            // WebClient를 사용하여 POST 요청을 보내고 응답을 동기적으로 처리
            return webClient.post()  // HTTP POST 요청을 전송
                    .uri(path)  // 요청할 경로를 설정 (path는 호출 시 전달된 값)
                    .body(requestBody != null ? BodyInserters.fromValue(requestBody) : BodyInserters.empty())
                    // 요청 본문(body)에 requestBody가 null이 아니면 JSON으로 변환하고, null이면 빈 본문 전송
                    .retrieve()  // 응답을 받아오기 시작 (응답을 받으면 이후 처리)
                    .toEntity(ResponseDto.class)  // 응답을 ResponseDto로 변환하여 ResponseEntity로 감싸서 반환
                    .block();  // 동기 처리: 이 메서드를 호출한 쓰레드는 응답을 받을 때까지 대기
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "WebClient POST 요청 중 오류 발생");
        }
    }

    // 공통으로 사용할 WebClient Put 요청 메서드 (동기 처리)
    public <T> ResponseEntity<ResponseDto> sendPutRequest(String baseUrl, String path, T requestBody) {
        try {
            // baseUrl과 path를 결합하여 요청 URI를 구성하고 WebClient를 생성
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            // WebClient를 사용하여 PUT 요청을 보내고 응답을 동기적으로 처리
            return webClient.put()  // HTTP PUT 요청을 전송
                    .uri(path)  // 요청할 경로를 설정 (path는 호출 시 전달된 값)
                    .body(requestBody != null ? BodyInserters.fromValue(requestBody) : BodyInserters.empty())
                    // 요청 본문(body)에 requestBody가 null이 아니면 JSON으로 변환하고, null이면 빈 본문 전송
                    .retrieve()  // 응답을 받아오기 시작 (응답을 받으면 이후 처리)
                    .toEntity(ResponseDto.class)  // 응답을 ResponseDto로 변환하여 ResponseEntity로 감싸서 반환
                    .block();  // 동기 처리: 이 메서드를 호출한 쓰레드는 응답을 받을 때까지 대기
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "WebClient PUT 요청 중 오류 발생");
        }
    }

    // 공통으로 사용할 WebClient Delete 요청 메서드 (동기 처리)
    public ResponseEntity<ResponseDto> sendDeleteRequest(String baseUrl, String path) {
        try {
            // baseUrl과 path를 결합하여 요청 URI를 구성하고 WebClient를 생성
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            // WebClient를 사용하여 DELETE 요청을 보내고 응답을 동기적으로 처리
            return webClient.delete()  // HTTP DELETE 요청을 전송
                    .uri(path)  // 요청할 경로를 설정 (path는 호출 시 전달된 값)
                    .retrieve()  // 응답을 받아오기 시작 (응답을 받으면 이후 처리)
                    .toEntity(ResponseDto.class)  // 응답을 ResponseDto로 변환하여 ResponseEntity로 감싸서 반환
                    .block();  // 동기 처리: 이 메서드를 호출한 쓰레드는 응답을 받을 때까지 대기
        } catch (Exception e) {
            throw new RestApiException(StatusCode.INTERNAL_SERVER_ERROR, "WebClient DELETE 요청 중 오류 발생");
        }
    }
}
