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
public class MovieRatingSchedule {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    /*
    * 매일 오전 1시 0분 0초에 영화 평균 평점 업데이트 배치 작업 시작
    * 배치 서버에서 가지고 있는 요약 테이블(평점)을 기준으로 업데이트 후
    * 업데이트 된 영화 정보를 기준으로 카프카 이벤트 발생
    * */
    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    public void runRatingJob() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String date = LocalDateTime.now().format(formatter);
        String uniqueId = UUID.randomUUID().toString();  // 고유한 UUID 추가

        log.info("runRatingJob Schedule Start at {}", date);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addString("runId", uniqueId)  // 고유한 값 추가
                .toJobParameters();

        try {
            jobLauncher.run(jobRegistry.getJob("AverageRatingCalcJob"), jobParameters);
        } catch (JobExecutionException e) {

            log.error("Error occurred while executing AverageRatingCalcJob: {}", e.getMessage(), e);
        } catch (Exception e) {

            log.error("Unexpected error occurred: {}", e.getMessage(), e);
        }
    }
}
