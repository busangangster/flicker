package com.flicker.movie.movie.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.movie.common.module.exception.RestApiException;
import com.flicker.movie.common.module.status.StatusCode;
import com.flicker.movie.movie.config.KafkaConfig;
import com.flicker.movie.movie.domain.entity.Movie;
import com.flicker.movie.movie.dto.MovieRatingEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomConsumer {

    private final KafkaConfig config;
    private KafkaConsumer<String, String> consumer = null;
    private final ObjectMapper objectMapper;
    private final MovieRepoUtil movieRepoUtil;

    @PostConstruct
    public void build() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, config.getConsumer().getGroupId());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, config.getConsumer().getKeyDeserializer());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.getConsumer().getValueDeserializer());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.getConsumer().getAutoOffsetReset());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, config.getConsumer().getMaxPollRecords());
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, config.getConsumer().getEnableAutoCommit());
        // UTF-8 설정 추가
        properties.setProperty("key.deserializer.encoding", "UTF-8");
        properties.setProperty("value.deserializer.encoding", "UTF-8");
        consumer = new KafkaConsumer<>(properties);
    }

    @KafkaListener(topics = "movie-rating")
    @Transactional
    public void consume(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String payload) {
        try {
            // 1. 역직렬화: payload를 MovieRatingEvent 객체로 변환
            MovieRatingEvent movieRatingEvent = objectMapper.readValue(payload, MovieRatingEvent.class);
            // 2. 영화 ID와 평점 추출
            int movieSeq = movieRatingEvent.getMovieSeq();
            double movieRating = movieRatingEvent.getMovieRating();
            // 3. DB에서 해당 영화 조회
            Movie movie = movieRepoUtil.findById(movieSeq);
            // 4. 영화 평점 업데이트
            movie.updateMovieRating(movieRating);
        } catch (Exception e) {
            throw new RestApiException(StatusCode.KAFKA_ERROR, "Kafka 이벤트 수신 중 오류가 발생했습니다.");
        }
    }

}