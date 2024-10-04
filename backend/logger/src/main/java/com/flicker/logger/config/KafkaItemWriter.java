package com.flicker.logger.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class KafkaItemWriter<T> implements ItemWriter<T> {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;
    private final ObjectMapper objectMapper;

    public KafkaItemWriter(KafkaTemplate<String, String> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());  // Java 8 DateTime 모듈 등록
    }

    @Override
    public void write(Chunk<? extends T> chunk) throws Exception {
        for (T item : chunk.getItems()) {
            String message = objectMapper.writeValueAsString(item);

            kafkaTemplate.send(topic, message);
            log.info("Sent message to Kafka topic {}: {}", topic, message);
        }
    }
}
