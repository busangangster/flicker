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
public class ModelUpdateSchedule {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Scheduled(cron = "0 0 0/6 * * *", zone = "Asia/Seoul")
    public void runRatingJob() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String date = LocalDateTime.now().format(formatter);
        String uniqueId = UUID.randomUUID().toString();  // 고유한 UUID 추가

        log.info("modelUpdate Schedule Start at {}", date);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .addString("runId", uniqueId)  // 고유한 값 추가
                .toJobParameters();

        try {
            jobLauncher.run(jobRegistry.getJob("modelUpdateJob"), jobParameters);
        } catch (JobExecutionException e) {

            log.error("Error occurred while executing modelUpdateJob: {}", e.getMessage(), e);
        } catch (Exception e) {

            log.error("Unexpected error occurred: {}", e.getMessage(), e);
        }
    }
}
