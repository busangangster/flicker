package com.flicker.bff.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // Java 8 날짜/시간 API 지원 (LocalDateTime 등)
        objectMapper.registerModule(new JavaTimeModule());

        // 필요한 경우 더 많은 모듈 추가 가능
        objectMapper.registerModule(new ParameterNamesModule());  // 생성자 파라미터 이름을 지원

        // 직렬화 시 timestamps 대신 ISO 8601 포맷 사용
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }
}