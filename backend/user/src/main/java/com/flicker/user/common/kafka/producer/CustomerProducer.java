package com.flicker.user.common.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flicker.user.common.kafka.dto.SentimentReview;
import com.flicker.user.common.kafka.dto.SentimentReviewSerializer;
import com.flicker.user.common.kafka.dto.WordCloudReview;
import com.flicker.user.common.kafka.dto.WordCloudReviewSerializer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerProducer {

    private KafkaProducer<String, SentimentReview> sentimentProducer;
    private KafkaProducer<String, WordCloudReview> wordCloudProducer;
    private final KafkaTemplate<String,String> kafkaTemplate;

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServer;


    @PostConstruct
    public void build() {
        // Kafka 프로듀서 설정
        Properties sentimentProperties = new Properties();
        sentimentProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);  // Kafka 서버 주소
        sentimentProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        sentimentProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SentimentReviewSerializer.class.getName());
        sentimentProducer = new KafkaProducer<>(sentimentProperties);

        Properties wordCloudProperties = new Properties();
        wordCloudProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);  // Kafka 서버 주소
        wordCloudProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        wordCloudProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, WordCloudReviewSerializer.class.getName());
        wordCloudProducer = new KafkaProducer<>(wordCloudProperties);


    }

    // Log 메시지 전송
    public void sendSentimentLog(SentimentReview msg) {
        ProducerRecord<String, SentimentReview> record = new ProducerRecord<>("sentiment-review", msg);

        sentimentProducer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception != null) {
                System.err.println("Error sending log message: " + exception.getMessage());
            } else {
                System.out.println("Log sent successfully to topic " + metadata.topic() + " partition " + metadata.partition() + " at offset " + metadata.offset());
            }
        });
    }

    // Log 메시지 전송
    public void sendWordCloudLog(WordCloudReview msg) {
        ProducerRecord<String, WordCloudReview> record = new ProducerRecord<>("wordcloud-review", msg);

        wordCloudProducer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception != null) {
                System.err.println("Error sending log message: " + exception.getMessage());
            } else {
                System.out.println("Log sent successfully to topic " + metadata.topic() + " partition " + metadata.partition() + " at offset " + metadata.offset());
            }
        });
    }

    // 리소스 정리
    public void close() {
        sentimentProducer.close();
        wordCloudProducer.close();
    }
}