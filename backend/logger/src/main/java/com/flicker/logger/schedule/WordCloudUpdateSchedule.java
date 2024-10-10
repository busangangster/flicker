package com.flicker.logger.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flicker.logger.application.WordCloudUpdateService;
import com.flicker.logger.dto.WordCloudResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@Slf4j
public class WordCloudUpdateSchedule {

    private final WordCloudUpdateService wordCloudUpdateService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public WordCloudUpdateSchedule(WordCloudUpdateService wordCloudUpdateService,
                                   KafkaTemplate<String, String> kafkaTemplate) {
        this.wordCloudUpdateService = wordCloudUpdateService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());  // JavaTimeModule 등록
    }

    @Scheduled(cron = "0 0 3 ? * SUN", zone = "Asia/Seoul")
    public void wordCloudUpdateSchedule() {

        log.info("Word Cloud Update Schedule");

        List<WordCloudResult> wordCloudUpdate = wordCloudUpdateService.getWordCloudUpdate();

        for (WordCloudResult result : wordCloudUpdate) {
            try {

                String message = objectMapper.writeValueAsString(result);
                kafkaTemplate.send("wordcloud-result", message);
                log.info("Sent message for movieSeq {}: {}", result.getMovieSeq(), message);
            } catch (Exception e) {

                log.error("Error serializing WordCloudResult for movieSeq {}: {}", result.getMovieSeq(), e.getMessage());
            }
        }
    }
}
