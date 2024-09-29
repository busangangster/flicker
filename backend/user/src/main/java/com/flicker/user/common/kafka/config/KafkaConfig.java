package com.flicker.user.common.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.kafka")
@Data
public class KafkaConfig {

    private String bootstrapServers;
    private Producer producer;
    private Template template;

    @Data
    public static class Producer {
        private String keySerializer;
        private String valueSerializer;
    }

    @Data
    public static class Template {
        private String defaultTopic;
    }
}