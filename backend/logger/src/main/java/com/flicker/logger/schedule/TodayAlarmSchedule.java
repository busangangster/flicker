package com.flicker.logger.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flicker.logger.dto.MovieAlarm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TodayAlarmSchedule {

    private final KafkaTemplate<String, String> kafkaTemplate;

//    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void TodayAlarm() {
        log.info("Today Alarm Schedule");
        MovieAlarm alarm = new MovieAlarm();
        alarm.setType("Today");
        alarm.setTimestamp(LocalDateTime.now());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String message = objectMapper.writeValueAsString(alarm);
            kafkaTemplate.send("alarm-movie", message);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
