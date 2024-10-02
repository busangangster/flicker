package com.flicker.logger.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flicker.logger.dto.MovieReviewEvent;
import com.flicker.logger.dto.SentimentReviewEvent;
import com.flicker.logger.dto.UserAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewConsumer {

    @Qualifier("dataDbJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

    @KafkaListener(topics = "movie-info")
    public void listenMovieInfo(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String payload) {

        log.info("Received MovieInfo message {}", payload);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            MovieReviewEvent reviewEvent = objectMapper.readValue(payload, MovieReviewEvent.class);

            String sql = "INSERT INTO movie_review_info(user_seq, review_seq, movie_seq, rating, type, action) " +
                    "VALUES(?,?,?,?,?,?)";

            jdbcTemplate.update(sql,
                    reviewEvent.getUserSeq(),
                    reviewEvent.getReviewSeq(),
                    reviewEvent.getMovieSeq(),
                    reviewEvent.getRating(),
                    reviewEvent.getType(),
                    reviewEvent.getAction());

            log.info("Saved MovieReviewEvent {}", reviewEvent);

        } catch (Exception e) {
            log.error("Error processing message: {}", payload, e);
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "sentiment-review")
    public void listenReviewSentiment(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String payload) {

        log.info("Received ReviewSentiment message {}", payload);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            SentimentReviewEvent reviewLog = objectMapper.readValue(payload, SentimentReviewEvent.class);

            String sql = "INSERT INTO sentiment_review_logs (review_seq, content, timestamp) " +
                    "VALUES (?, ?, ?)";

            jdbcTemplate.update(sql,
                    reviewLog.getReviewSeq(),
                    reviewLog.getContent(),
                    reviewLog.getTimestamp());

            log.info("Saved SReview {}", reviewLog);
        } catch (Exception e) {
            log.error("Error processing message: {}", payload, e);
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "user-action")
    public void listenUserAction(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Payload String payload) {

        log.info("Received UserAction message {}", payload);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            UserAction userAction = objectMapper.readValue(payload, UserAction.class);

            String sql = "INSERT INTO user_action_logs (user_seq, keyword, action, timestamp) " +
                    "VALUES(?,?,?,?)";

            jdbcTemplate.update(sql,
                    userAction.getUserSeq(),
                    userAction.getKeyword(),
                    userAction.getAction(),
                    userAction.getTimestamp());

            log.info("Saved UserAction {}", userAction);
        } catch (Exception e) {
            log.error("Error processing message: {}", payload, e);
            throw new RuntimeException(e);
        }
    }
}