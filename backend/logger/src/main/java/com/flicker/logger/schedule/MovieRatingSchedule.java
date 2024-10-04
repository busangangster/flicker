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
public class MovieRatingSchedule {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    /*
    * 매일 오전 1시 0분 0초에 영화 평균 평점 업데이트 배치 작업 시작
    * 배치 서버에서 가지고 있는 요약 테이블(평점)을 기준으로 업데이트 후
    * 업데이트 된 영화 정보를 기준으로 카프카 이벤트 발생
    * */
    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    public void runRatingJob() throws Exception{

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = dateFormat.format(new Date());

        log.info("runRatingJob Schedule Start at {}", date);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("AverageRatingCalcJob"), jobParameters);
    }
}
