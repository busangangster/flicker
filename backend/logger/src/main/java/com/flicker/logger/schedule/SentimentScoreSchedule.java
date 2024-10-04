package com.flicker.logger.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

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
//    @Scheduled(cron = "0 */10 * * * ?")
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void runRatingJob() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = dateFormat.format(new Date());

        log.info("SentimentScoreJob Schedule Start at {}", date);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("SentimentScoreJob"), jobParameters);
    }
}
