package com.flicker.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class WebConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();

     //   config.addAllowedOrigin("http://192.168.30.180:5173");
     //   config.addAllowedOrigin("http://localhost:5173");
     //   config.addAllowedOrigin("http://j11e206.p.ssafy.io:5173");
     //   config.addAllowedOrigin("http://192.168.30.195:5173");
     //   config.addAllowedOrigin("https://j11e206.p.ssafy.io:5173");
        config.addAllowedOrigin("https://j11e206.p.ssafy.io");

        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");

        config.addAllowedHeader("*");  // 모든 헤더 허용
        config.addExposedHeader("Authorization");  // 노출할 헤더
        config.setAllowCredentials(true);  // 자격 증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // 모든 경로에 대해 CORS 설정 적용

        return new CorsWebFilter(source);
    }
}
