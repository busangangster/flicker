package com.flicker.gateway.jwt;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndPoints = List.of(
            "api/bff",
            "/api/bff/client",
            "/api/bff/movie"
    );

    public static final List<String> openApiEndPointDetails = List.of(
            "/api/bff/user",
            "/api/bff/user/",
            "/api/bff/user/login",
            "/api/bff/user/refresh",
            "/api/user/login"
    );

    /*
     * 요청 경로가 해당 엔드포인트와 일치하거나 하위 URL을 포함하는지 확인하는 Predicate
     */
    public Predicate<ServerHttpRequest> isSecured = request -> {
        String requestPath = request.getURI().getPath();

        // openApiEndPoints에 경로가 포함되어 있거나,
        // openApiEndPointDetails에 정확히 일치하는 경로가 있는지 확인
        boolean isInOpenApiEndpoints = openApiEndPoints
                .stream()
                .anyMatch(requestPath::contains);

        boolean isInOpenApiEndPointDetails = openApiEndPointDetails
                .stream()
                .anyMatch(requestPath::equals);

        // 두 조건 중 하나라도 만족하면 보안이 필요 없는 경로로 판단
        return !(isInOpenApiEndpoints || isInOpenApiEndPointDetails);
    };
}
