package com.flicker.gateway.jwt;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndPoints = List.of(
            "/api/bff/user",
            "/api/bff/user/",
            "/api/bff/user/login"
    );

    /*
    * 요청 경로가 해당 엔드포린트와 하위 URL을 하용하는 코드
    * */
//    public Predicate<ServerHttpRequest> isSecured =
//            request -> openApiEndPoints
//                    .stream()
//                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    // 요청 경로가 허용된 엔드포인트와 정확히 일치하는지 확인
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndPoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().equals(uri));
}