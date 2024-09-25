package com.flicker.movie.movie.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.movie.common.module.exception.RestApiException;
import com.flicker.movie.common.module.status.StatusCode;
import com.flicker.movie.movie.config.KafkaConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import java.util.Properties;

/*
    * CustomProducer 클래스는 Kafka 메시지를 발행하는 프로듀서 역할을 한다.
    * KafkaConfig, ObjectMapper를 주입받는다.
    * build() 메서드에서 KafkaProducer 객체를 생성한다.
    * send() 메서드는 MovieEvent 객체를 JSON으로 직렬화하여 Kafka로 전송한다.
    * @PostConstruct 어노테이션을 사용하여 빈이 생성될 때 build() 메서드를 실행한다.
    * @PreDestroy 어노테이션을 사용하여 빈이 소멸될 때 closeProducer() 메서드를 실행한다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomProducer {

    private final KafkaConfig config;
    private KafkaProducer<String, String> producer = null;
    private final ObjectMapper objectMapper;  // Jackson ObjectMapper로 JSON 직렬화

    @PostConstruct
    public void build() {
        // Kafka Producer 설정
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getProducer().getKeySerializer());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getProducer().getValueSerializer());
        // 재시도 설정
        properties.put(ProducerConfig.RETRIES_CONFIG, 5);  // 최대 5번 재시도
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 2000);  // 재시도 간의 대기 시간 2초
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000); // 메시지 전송 최대 허용 시간 (2분)
        properties.put(ProducerConfig.ACKS_CONFIG, "all");  // 모든 브로커가 메시지를 확인할 때까지 기다림
        // UTF-8 설정 추가
        properties.setProperty("key.serializer.encoding", "UTF-8");
        properties.setProperty("value.serializer.encoding", "UTF-8");
        producer = new KafkaProducer<>(properties);
    }

    // MovieEvent 객체를 JSON으로 직렬화하여 Kafka로 전송
    public <T> void send(T event, String topic) {
        try {
            if (topic.equals("movieInfo")) {
                topic = config.getTemplate().getMovieInfoTopic();
            }

            // 객체를 JSON으로 직렬화
            String jsonMessage = objectMapper.writeValueAsString(event);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, jsonMessage);

            producer.send(record, (metadata, exception) -> {
                String logMessage = String.format("Publishing message: %s", jsonMessage);
                if (exception != null) {
                    // 전송 실패 시 로깅
                    log.error("{} failed due to {}", logMessage, exception.getMessage());
                } else {
                    // 전송 성공 시 로깅
                    log.info("{} succeeded - partition: {}, offset: {}", logMessage, metadata.partition(), metadata.offset());
                }
            });
        } catch (Exception e) {
            throw new RestApiException(StatusCode.KAFKA_ERROR, "Kafka 이벤트 발행 중 오류가 발생했습니다.");
        }
    }

    @PreDestroy
    public void closeProducer() {
        if (producer != null) {
            producer.close();
        }
    }
}
