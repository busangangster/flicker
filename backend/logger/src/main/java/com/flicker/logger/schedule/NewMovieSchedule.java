package com.flicker.logger.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flicker.logger.application.NewMovieService;
import com.flicker.logger.application.NewMovieUpdateService;
import com.flicker.logger.dto.NewMovieDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NewMovieSchedule {

    private final NewMovieService newMovieService;
    private final NewMovieUpdateService newMovieUpdateService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public NewMovieSchedule(NewMovieService newMovieService, NewMovieUpdateService newMovieUpdateService,
                            KafkaTemplate<String, String> kafkaTemplate) {
        this.newMovieService = newMovieService;
        this.newMovieUpdateService = newMovieUpdateService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());  // JavaTimeModule 등록
    }

    @Scheduled(cron = "0 0 0 L * ?", zone = "Asia/Seoul")
    public void scheduleNewMovie() {
        log.info("New movie schedule");

        List<NewMovieDto> newMovieDtos = newMovieService.getNewMovies();
        newMovieUpdateService.updateWordCloud(newMovieDtos);
        List<Integer> movieSeqList = new ArrayList<>();
        for (NewMovieDto newMovieDto : newMovieDtos) {
            movieSeqList.add(newMovieDto.getMovieSeq());
        }

        try {
            String message = objectMapper.writeValueAsString(movieSeqList);
            kafkaTemplate.send("new-movie", message);
            log.info("New movie schedule completed");
        } catch (Exception e) {
            log.error("New movie schedule failed", e);
        }

    }
}
