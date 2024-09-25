//package com.flicker.logger.schedule;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.configuration.JobRegistry;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Configuration
//@RequiredArgsConstructor
//public class MovieRatingSchedule {
//
//    private final JobLauncher jobLauncher;
//    private final JobRegistry jobRegistry;
//
//    @Scheduled(cron = "10 * * * * *", zone = "Asia/Seoul")
//    public void runRatingJob() throws Exception{
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
//        String date = dateFormat.format(new Date());
//
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("date", date)
//                .toJobParameters();
//
//        jobLauncher.run(jobRegistry.getJob("calculateAverageRatingJob"), jobParameters);
//    }
//}
