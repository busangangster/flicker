package com.flicker.logger.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SentimentScoreSchedule {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    /*
     * 매 10분 주기로 영화 리뷰에 대한 감성 분석 결과 업데이트
     * 추천 서버에서 KOBERT 모델을 API로 호출한 결과를 가지고 테이블 업데이트 후
     * 감성분석 결과를 회원 서비스로 Kafka 발행
     * */
    @Scheduled(cron = "0 */10 * * * ?", zone = "Asia/Seoul")
    public void runRatingJob() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String date = LocalDateTime.now().format(formatter);
        String uniqueId = UUID.randomUUID().toString();  // 고유한 UUID 추가

        log.info("SentimentScoreJob Schedule Start at {}", date);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addString("runId", uniqueId)  // 고유한 값 추가
                .toJobParameters();

        try {
            jobLauncher.run(jobRegistry.getJob("SentimentScoreJob"), jobParameters);
        } catch (JobExecutionException e) {

            log.error("Error occurred while executing SentimentScoreJob: {}", e.getMessage(), e);
        } catch (Exception e) {

            log.error("Unexpected error occurred: {}", e.getMessage(), e);
        }

    }
}
