//package com.flicker.logger.batch;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//@RequiredArgsConstructor
//@Slf4j
//public class CollaboRecommendModelUpdateBatchConfig {
//
//    private final JobRepository jobRepository;
//    private final PlatformTransactionManager transactionManager;
//    @Qualifier("dataDBSource") private final DataSource dataSource;
//
//    @Bean
//    public Job sentimentBatchScoreJob() {
//
//        log.info("modelUpdate batch score job started");
//
//        return new JobBuilder("modelUpdateJob", jobRepository)
//                .start(modelUpdateStep())
//                .build();
//    }
//
//    @Bean
//    public Step modelUpdateStep() {
//
//        log.info()
//
//    }
//}
