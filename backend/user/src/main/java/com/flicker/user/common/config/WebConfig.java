//package com.flicker.user.common.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")  // 모든 경로에 대해
//                .allowedOrigins("https://j11e206.p.ssafy.io")  // 특정 IP와 포트 허용
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")  // 모든 헤더 허용
//                .exposedHeaders("Authorization")
//                .allowCredentials(true);  // 자격 증명(쿠키 등) 허용
//    }
//}
