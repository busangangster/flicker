package com.flicker.logger.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * 스케쥴러 수동 실행을 위한 스케쥴러 (ADMIN)
 * */
@Controller
@ResponseBody
@RequiredArgsConstructor
public class MainController {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @GetMapping("/rating")
    public String startBatchJob() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("AverageRatingCalcJob"), jobParameters);

        return "ok";
    }

    @GetMapping("/rating2")
    public String startBatchJob2() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("SentimentScoreJob"), jobParameters);

        return "ok";
    }

    @GetMapping("/test")
    public String test() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("modelUpdateJob"), jobParameters);

        return "ok";
    }
}
