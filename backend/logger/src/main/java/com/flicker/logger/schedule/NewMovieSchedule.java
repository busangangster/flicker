package com.flicker.logger.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flicker.logger.application.NewMovieService;
import com.flicker.logger.application.NewMovieUpdateService;
import com.flicker.logger.dto.ActorDto;
import com.flicker.logger.dto.ActorRequest;
import com.flicker.logger.dto.NewMovieDto;
import com.flicker.logger.dto.NewMovieUpdateRequest;
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

        List<Integer> movieSeqList = new ArrayList<>();
        List<NewMovieUpdateRequest> newMovies = new ArrayList<>();

        for (NewMovieDto newMovieDto : newMovieDtos) {

            movieSeqList.add(newMovieDto.getMovieSeq());

            if (newMovieDto.isNewCheck()) {
                NewMovieUpdateRequest newMovieUpdateRequest = new NewMovieUpdateRequest();
                newMovieUpdateRequest.setMovieSeq(newMovieDto.getMovieSeq());
                newMovieUpdateRequest.setMovieTitle(newMovieDto.getMovieTitle());
                newMovieUpdateRequest.setMovieYear(newMovieDto.getMovieYear());
                newMovieUpdateRequest.setGenre(newMovieDto.getGenre());
                List<ActorRequest> actorRequestList = new ArrayList<>();
                List<ActorDto> actors = newMovieDto.getActors();
                for (ActorDto actorDto : actors) {
                    ActorRequest actorRequest = new ActorRequest();
                    actorRequest.setActorName(actorDto.getActorName());
                    actorRequestList.add(actorRequest);
                }
                newMovieUpdateRequest.setActors(actorRequestList);
                newMovies.add(newMovieUpdateRequest);
            }
        }

        newMovieUpdateService.newMovieUpdate(newMovies);

        try {
            String message = objectMapper.writeValueAsString(movieSeqList);
            kafkaTemplate.send("new-movie", message);
            log.info("New movie schedule completed");
        } catch (Exception e) {
            log.error("New movie schedule failed", e);
        }

    }
}
