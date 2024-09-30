package com.flicker.gateway.jwt;

import com.flicker.gateway.exception.GatewayException;
import com.flicker.gateway.status.StatusCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class AuthenticationFilter  extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;

    private final JWTUtil jwtUtil;

    public AuthenticationFilter (JWTUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.routeValidator = new RouteValidator();
    }

    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // RouteValidator를 사용하여 해당 경로가 인증 예외 경로인지 확인
            if (routeValidator.isSecured.test(request)) {
                String token = getTokenFromRequest(request);

                // 토큰이 없는 경우 처리
                if (token == null) {
                    throw new GatewayException(StatusCode.NO_SUCH_ELEMENT, "JWT Token is missing");
                }

                try {
                    // 토큰 만료 확인
                    jwtUtil.isTokenExpired(token);
                } catch (ExpiredJwtException e) {
                    // 만료된 토큰 처리
                    throw new GatewayException(StatusCode.NO_SUCH_ELEMENT, "JWT Token is expired");
                } catch (JwtException | IllegalArgumentException e) {
                    // 유효하지 않은 토큰 처리
                    throw new GatewayException(StatusCode.FORBIDDEN_ACCESS, "Invalid JWT Token");
                }

                // 토큰에서 사용자 이름 추출 후 헤더에 추가
                String username = jwtUtil.getUserId(token);
                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-auth-userId", username).build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build())
                        .then(Mono.fromRunnable(() -> {
                            ServerHttpResponse response = exchange.getResponse();
                            log.info("AuthenticationFilter : response code : {}", response.getStatusCode());
                        }));
            }

            // 인증이 필요 없는 경로는 그냥 필터 체인을 타도록 허용
            return chain.filter(exchange);
        });
    }

    private String getTokenFromRequest(ServerHttpRequest request) {

        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {

        exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(HttpStatus.SC_UNAUTHORIZED));
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

        byte[] bytes = String.format("{\"error\": \"%s\"}", message).getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Config {
        private String baseMessage;
        private boolean pre;
        private boolean post;
    }
}