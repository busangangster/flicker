package com.flicker.movie.movie.application;

import com.flicker.movie.movie.config.KafkaConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomProducer {

    private final KafkaConfig config;  // Kafka 설정을 주입받는 Config 객체
    private KafkaProducer<String, String> producer = null;  // Kafka 프로듀서를 관리하는 객체

    /**
     * Kafka 프로듀서를 초기화하는 메서드.
     * @PostConstruct 어노테이션으로 인해 클래스 생성 후 자동으로 호출됨.
     */
    @PostConstruct
    public void build() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers()); // Kafka 브로커 주소 설정
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getKeySerializer()); // 메시지 키 직렬화 설정
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getValueSerializer()); // 메시지 값 직렬화 설정
        producer = new KafkaProducer<>(properties); // Kafka 프로듀서 생성
    }

    /**
     * Kafka에 메시지를 전송하는 메서드.
     * ProducerRecord를 생성하여 전송하며, 전송 후 콜백으로 성공 여부를 로깅.
     *
     * @param message 전송할 메시지
     */
    public void send(String message) {
        // 메시지를 전송할 Kafka 토픽과 메시지를 포함한 ProducerRecord 객체 생성
        ProducerRecord<String, String> record = new ProducerRecord<>(config.getDefaultTopic(), message);

        // 메시지 전송
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                // 메시지 전송 실패 시 에러 로깅
                log.error("Failed to publish message: {} due to {}", message, exception.getMessage());
            } else {
                // 메시지 전송 성공 시 파티션과 오프셋 정보를 로깅
                log.info("Successfully published message: {} to partition: {}, offset: {}", message, metadata.partition(), metadata.offset());
            }
        });
    }

    /**
     * Kafka 프로듀서를 종료하는 메서드.
     * @PreDestroy 어노테이션으로 인해 애플리케이션이 종료될 때 자동으로 호출됨.
     */
    @PreDestroy
    public void closeProducer() {
        if (producer != null) {
            producer.close(); // Kafka 프로듀서 종료
            log.info("Kafka producer closed."); // 종료 로그 출력
        }
    }
}
