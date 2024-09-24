//package com.flicker.logger.batch;
//
//import com.flicker.logger.entity.MovieAverageRating;
//import com.flicker.logger.entity.Review;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.data.builder.MongoCursorItemReaderBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import java.util.HashMap;
//
//
//@Configuration
//@RequiredArgsConstructor
//@EnableBatchProcessing
//@Slf4j
//public class MovieRatingCalcBatch {
//
//    private final JobRepository jobRepository;
//    private final PlatformTransactionManager platformTransactionManager;
//    private final MongoTemplate mongoTemplate;
//
//    @Bean
//    public Job ratingCalcJob() {
//
//        log.info("movie rating calculation batch started");
//
//        return new JobBuilder("calculateAverageRatingJob", jobRepository)
//                .start(ratingStep())
//                .build();
//    }
//
//    @Bean
//    public Step ratingStep() {
//
//        log.info("movie rating calculation step started");
//
//        return new StepBuilder("firstStep", jobRepository)
//                .<Review, MovieAverageRating>chunk(10, platformTransactionManager)
//                .reader(mongoReviewReader())
//                .processor(reviewProcessor())
//                .writer(averageRatingWriter())
//                .build();
//    }
//
//    @Bean
//    public ItemReader<Review> mongoReviewReader() {
//
//        return new MongoCursorItemReaderBuilder<Review>()
//                .name("mongoReviewReader")
//                .template(mongoTemplate)
//                .targetType(Review.class)
//                .jsonQuery("{}")
//                .sorts(new HashMap<>())
//                .build();
//    }
//
//    @Bean
//    public ItemProcessor<Review, MovieAverageRating> reviewProcessor() {
//        return new ReviewProcessor(); // 각 영화별로 리뷰 데이터를 그룹화하고 평균을 계산
//    }
//
//    @Bean
//    public ItemWriter<MovieAverageRating> averageRatingWriter() {
//        return new ConsoleItemWriter(); // 평균 평점을 콘솔에 출력하거나 저장
//    }
//}
