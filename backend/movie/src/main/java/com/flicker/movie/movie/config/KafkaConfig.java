package com.flicker.movie.movie.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Producer producer;

    private Consumer consumer;

    private Template template;

    @Data
    public static class Producer {
        private String keySerializer;
        private String valueSerializer;
    }

    @Data
    public static class Consumer {
        private String groupId;
        private String enableAutoCommit;
        private String autoOffsetReset;
        private String keyDeserializer;
        private String valueDeserializer;
        private String maxPollRecords;
    }

    @Data
    public static class Template {
        private String defaultTopic;
    }
}
