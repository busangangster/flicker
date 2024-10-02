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

    @KafkaListener(topics = "movie-info", groupId = "review-group")
    public void listenMovieInfo(String message) {

        log.info("Received MovieInfo message {}", message);

        try {
            ObjectMapper mapper = new ObjectMapper();
            MovieReviewEvent reviewEvent = mapper.readValue(message, MovieReviewEvent.class);

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
            log.error("Error processing message: {}", message, e);
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

    @KafkaListener(topics = "user-action", groupId = "review-group")
    public void listenUserAction(String message) {

        log.info("Received UserAction message {}", message);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            UserAction userAction = objectMapper.readValue(message, UserAction.class);

            String sql = "INSERT INTO user_action_logs (user_seq, keyword, action, timestamp) " +
                    "VALUES(?,?,?,?)";

            jdbcTemplate.update(sql,
                    userAction.getUserSeq(),
                    userAction.getKeyword(),
                    userAction.getAction(),
                    userAction.getTimestamp());

            log.info("Saved UserAction {}", userAction);
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
            throw new RuntimeException(e);
        }
    }
}