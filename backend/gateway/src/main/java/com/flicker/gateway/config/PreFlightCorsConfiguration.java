package com.flicker.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Configuration
public class PreFlightCorsConfiguration {

    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type";
    private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS";
    private static final String MAX_AGE = "3600";
    private static final String ALLOWED_CREDENTIALS = "true";

    // 허용할 도메인의 리스트
    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "https://j11e206.p.ssafy.io",
            "http://localhost:5173"
    );

    @Bean
    public WebFilter corsFilter() {

        return (ServerWebExchange ctx, WebFilterChain chain) -> {

            ServerHttpRequest request = ctx.getRequest();
            ServerHttpResponse response = ctx.getResponse();
            HttpHeaders headers = response.getHeaders();

            String origin = request.getHeaders().getOrigin();

            if (CorsUtils.isPreFlightRequest(request)) {
                // 요청의 Origin이 허용된 도메인 리스트에 있는지 확인
                if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
                    headers.add("Access-Control-Allow-Origin", origin);
                    headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                    headers.add("Access-Control-Max-Age", MAX_AGE);
                    headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
                    headers.add("Access-Control-Allow-Credentials", ALLOWED_CREDENTIALS);
                }

                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }

            return chain.filter(ctx);
        };
    }
}
