package com.flicker.logger.batch;

import com.flicker.logger.dto.MovieReviewEvent;
import com.flicker.logger.service.ModelUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CollaboRecommendModelUpdateBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ModelUpdateService modelUpdateService;
    @Qualifier("dataDBSource") private final DataSource dataSource;

    @Bean
    public Job modelUpdateJob() {

        log.info("modelUpdate batch score job started");

        return new JobBuilder("modelUpdateJob", jobRepository)
                .start(modelUpdateStep())
                .build();
    }

    @Bean
    public Step modelUpdateStep() {

        log.info("modelUpdate batch score step started");

        return new StepBuilder("modelUpdateStep", jobRepository)
                .<MovieReviewEvent, MovieReviewEvent>chunk(1000, transactionManager)
                .reader(modelUpdateBatchReader())
                .processor(modelUpdateBatchProcessor())
                .writer(ModelUpdatetCompositeItemWritercompositeItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<MovieReviewEvent> modelUpdateBatchReader() {

        return new JdbcCursorItemReaderBuilder<MovieReviewEvent>()
                .dataSource(dataSource)
                .name("reviewLogsReader")
                .sql("SELECT review_seq, user_seq, movie_seq, rating, action, sentiment_score " +
                        "FROM data_db.movie_review_info " +
                        "WHERE is_deleted = 0 " +
                        "AND (action = 'DELETE' OR sentiment_score IS NOT NULL);")
                .rowMapper(new BeanPropertyRowMapper<>(MovieReviewEvent.class))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<MovieReviewEvent, MovieReviewEvent> modelUpdateBatchProcessor() {

        return items -> items;
    }

    @Bean
    @StepScope
    public ItemWriter<MovieReviewEvent> modelUpdateItemReader(ModelUpdateService modelUpdateService) {

        return items -> {
            List<MovieReviewEvent> movieReviewEvents = new ArrayList<>();
            items.forEach(movieReviewEvent -> {
                MovieReviewEvent review = new MovieReviewEvent();
                review.setUserSeq(movieReviewEvent.getUserSeq());
                review.setMovieSeq(movieReviewEvent.getMovieSeq());
                review.setRating(movieReviewEvent.getRating());
                review.setAction(movieReviewEvent.getAction());
                review.setSentimentScore(movieReviewEvent.getSentimentScore());
                movieReviewEvents.add(review);
            });
            modelUpdateService.updateModel(movieReviewEvents);
        };
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<MovieReviewEvent> modelUpdateJdbcBatchItemWriter() {

        return new JdbcBatchItemWriterBuilder<MovieReviewEvent>()
                .dataSource(dataSource)
                .sql("UPDATE data_db.movie_review_info " +
                        "SET is_deleted = 1 " +
                        "WHERE review_seq = :reviewSeq")
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public CompositeItemWriter<MovieReviewEvent> ModelUpdatetCompositeItemWritercompositeItemWriter() {
        CompositeItemWriter<MovieReviewEvent> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(modelUpdateItemReader(modelUpdateService), modelUpdateJdbcBatchItemWriter()));
        return writer;
    }
}
