package com.flicker.logger.batch;

import com.flicker.logger.dto.MovieAverageRating;
import com.flicker.logger.dto.MovieRating;
import com.flicker.logger.dto.MovieReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MovieRatingBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Qualifier("dataDBSource") private final DataSource dataSource;

//    private final List<MovieAverageRating> writtenItems = new ArrayList<>();

    // 1. Job 정의
    @Bean
    public Job ratingCalcJob() {
        log.info("Movie rating calculation batch started");
        return new JobBuilder("AverageRatingCalcJob", jobRepository)
                .start(ratingStep())
//                .next(sendToKafkaStep())
                .build();
    }

//    @Bean
//    public Step sendToKafkaStep() {
//
//        return new StepBuilder("sendToKafkaStep", jobRepository)
//                .<MovieAverageRating, MovieRating>chunk(10, platformTransactionManager)
//                .reader(sendToKafkaReader()) // 평균 점수를 읽어오는 Reader
//                .processor(sendToKafkaProcessor())
//                .writer(kafkaItemWriter()) // 카프카로 발행하는 Writer
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public JdbcCursorItemReader<MovieAverageRating> sendToKafkaReader() {
//        // JdbcCursorItemReader<MovieAverageRating>를 반환
//        return new JdbcCursorItemReaderBuilder<MovieAverageRating>()
//                .dataSource(dataSource)
//                .name("movieRatingReader")
//                .sql("SELECT movie_seq, movie_count, movie_total_rating " +
//                        "FROM data_db.movie_average_rating " +
//                        "WHERE movie_count > 0")
//                .rowMapper(new BeanPropertyRowMapper<>(MovieAverageRating.class))
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public ItemProcessor<MovieAverageRating, MovieRating> sendToKafkaProcessor() {
//        return reviewLog -> new MovieRating(reviewLog.getMovieSeq(), reviewLog.getAverageRating());
//    }
//
//    @Bean
//    @StepScope
//    public ItemWriter<>

    @Bean
    @StepScope
    public JdbcBatchItemWriter<MovieAverageRating> kafkaItemWriter() {

        return new JdbcBatchItemWriterBuilder<MovieAverageRating>()
                .dataSource(dataSource)
                .sql("UPDATE data_db.movie_review_info SET is_processed = true WHERE review_seq = :reviewSeq")
                .beanMapped()
                .build();
    }

    @Bean
    public Step ratingStep() {
        log.info("Movie rating calculation step started");

        return new StepBuilder("ratingStep", jobRepository)
                .<MovieReviewEvent, MovieAverageRating>chunk(10, platformTransactionManager) // 변경: <입력 타입, 출력 타입>
                .reader(movieRatingReader())
                .processor(movieRatingProcessor())
                .writer(compositeItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<MovieReviewEvent> movieRatingReader() {
        // JdbcCursorItemReader<MovieReviewEvent>를 반환
        return new JdbcCursorItemReaderBuilder<MovieReviewEvent>()
                .dataSource(dataSource)
                .name("movieRatingReader")
                .sql("SELECT user_seq, review_seq, movie_seq, rating, type, action, timestamp " +
                        "FROM data_db.movie_review_info " +
                        "WHERE is_processed = 0")
                .rowMapper(new BeanPropertyRowMapper<>(MovieReviewEvent.class))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<MovieReviewEvent, MovieAverageRating> movieRatingProcessor() {
        return reviewLog -> {
            if (!"REVIEW".equals(reviewLog.getType())) {
                return null;
            }

            MovieAverageRating rating = new MovieAverageRating();

            if ("CREATE".equals(reviewLog.getAction())) {
                rating.setMovieCount(1);
                rating.setMovieTotalRating(reviewLog.getRating());
            } else if ("DELETE".equals(reviewLog.getAction())) {
                rating.setMovieCount(-1);
                rating.setMovieTotalRating(reviewLog.getRating());
            }

            rating.setMovieSeq(reviewLog.getMovieSeq());
            rating.setReviewSeq(reviewLog.getReviewSeq());
            return rating;
        };
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<MovieAverageRating> movieRatingWriter() {
        return new JdbcBatchItemWriterBuilder<MovieAverageRating>()
                .dataSource(dataSource)
                .sql("INSERT INTO data_db.movie_average_rating (movie_seq, movie_count, movie_total_rating) " +
                        "VALUES (:movieSeq, :movieCount, :movieTotalRating) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "movie_count = movie_count + :movieCount, " +  // 기존 값에 새로운 값 더하기
                        "movie_total_rating = movie_total_rating + :movieTotalRating")  // 기존 값에 새로운 값 더하기
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<MovieAverageRating> logStatusUpdateWriter() {

        return new JdbcBatchItemWriterBuilder<MovieAverageRating>()
                .dataSource(dataSource)
                .sql("UPDATE data_db.movie_review_info SET is_processed = true WHERE review_seq = :reviewSeq")
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public CompositeItemWriter<MovieAverageRating> compositeItemWriter() {
        CompositeItemWriter<MovieAverageRating> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(movieRatingWriter(), logStatusUpdateWriter()));
        return writer;
    }
}
