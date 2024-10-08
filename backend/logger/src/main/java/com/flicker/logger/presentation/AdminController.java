package com.flicker.logger.presentation;

import com.flicker.logger.application.WordCloudUpdateService;
import com.flicker.logger.dto.KeywordCount;
import com.flicker.logger.dto.WordCloudResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/*
 * 스케쥴러 수동 실행을 위한 스케쥴러 (ADMIN)
 * */
@Controller
@ResponseBody
@RequiredArgsConstructor
@Slf4j  // 로그 사용을 위한 어노테이션 추가
public class AdminController {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final WordCloudUpdateService wordCloudUpdateService;

    @GetMapping("/averageRating")
    public void startBatchJob() {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("Starting AverageRatingCalcJob at {}", System.currentTimeMillis());
            jobLauncher.run(jobRegistry.getJob("AverageRatingCalcJob"), jobParameters);
        } catch (JobExecutionException e) {
            log.error("Error occurred while executing AverageRatingCalcJob: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage(), e);
        }
    }

    @GetMapping("/sentimentScore")
    public void startSentimentBatchJob() {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("Starting SentimentScoreJob at {}", System.currentTimeMillis());
            jobLauncher.run(jobRegistry.getJob("SentimentScoreJob"), jobParameters);
        } catch (JobExecutionException e) {
            log.error("Error occurred while executing SentimentScoreJob: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage(), e);
        }
    }

    @GetMapping("/model-update")
    public void modelUpdateJob() {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("Starting modelUpdateJob at {}", System.currentTimeMillis());
            jobLauncher.run(jobRegistry.getJob("modelUpdateJob"), jobParameters);
        } catch (JobExecutionException e) {
            log.error("Error occurred while executing modelUpdateJob: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage(), e);
        }
    }
}
