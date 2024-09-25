package com.flicker.logger.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.logger.dto.MovieReviewEvent;
import com.flicker.logger.dto.SentimentReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewConsumer {

    @Qualifier("dataDbJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

    @KafkaListener(topics = "movie_info", groupId = "review-group")
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

    @KafkaListener(topics = "sentiment-review", groupId = "review-group")
    public void listenReviewSentiment(String message) {

        log.info("Received ReviewSentiment message {}", message);

        try {
            ObjectMapper mapper = new ObjectMapper();
            SentimentReviewEvent reviewLog = mapper.readValue(message, SentimentReviewEvent.class);

            String sql = "INSERT INTO review_logs (review_seq, content, timestamp) " +
                    "VALUES (?, ?, ?)";

            jdbcTemplate.update(sql,
                    reviewLog.getReviewSeq(),
                    reviewLog.getContent(),
                    reviewLog.getTimeStamp());

            log.info("Saved SReview {}", reviewLog);
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
            throw new RuntimeException(e);
        }
    }
}