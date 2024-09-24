package com.flicker.logger.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flicker.logger.entity.Review;
import com.flicker.logger.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewConsumer {

    private final ReviewRepository reviewRepository;

    @KafkaListener(topics = "daily-reviews", groupId = "review-group")
    public void listenReview(String message) {

        log.info("Received message {}", message);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Review review = mapper.readValue(message, Review.class);

            reviewRepository.save(review);
            log.info("Saved Review {}", review);
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
}