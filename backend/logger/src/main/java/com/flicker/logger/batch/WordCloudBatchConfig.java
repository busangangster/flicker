package com.flicker.logger.batch;

import com.flicker.logger.application.WordCloudUpdateService;
import com.flicker.logger.dto.WordCloudDto;
import com.flicker.logger.dto.WordCloudRequest;
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
public class WordCloudBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final WordCloudUpdateService wordCloudUpdateService;
    @Qualifier("dataDBSource") private final DataSource dataSource;

    @Bean
    public Job wordCloudJob() {

        log.info("wordCloud batch score job started");

        return new JobBuilder("wordCloudJob", jobRepository)
                .start(wordCloudStep())
                .build();
    }

    @Bean
    public Step wordCloudStep() {

        log.info("wordCloud batch step started");

        return new StepBuilder("wordCloudStep", jobRepository)
                .<WordCloudDto, WordCloudDto>chunk(1000, transactionManager)
                .reader(wordCloudBatchReader())
                .processor(wordCloudUpdateBatchProcessor())
                .writer(modelUpdatetCompositeItemWritercompositeItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<WordCloudDto> wordCloudBatchReader() {

        return new JdbcCursorItemReaderBuilder<WordCloudDto>()
                .dataSource(dataSource)
                .name("wordCloudStep")
                .sql("SELECT id, movie_seq, content FROM data_db.wordcloud_review_logs " +
                        "WHERE is_processed = 0")
                .rowMapper(new BeanPropertyRowMapper<>(WordCloudDto.class))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<WordCloudDto, WordCloudDto> wordCloudUpdateBatchProcessor() {
        return items -> items;
    }

    @Bean
    @StepScope
    public ItemWriter<WordCloudDto> wordCloudUpdateItemWriter(WordCloudUpdateService wordCloudUpdateService) {

        return items -> {
            List<WordCloudRequest> wordCloudEvents = new ArrayList<>();
            items.forEach(movieReviewEvent -> {
                WordCloudRequest req = new WordCloudRequest();
                req.setMovieSeq(movieReviewEvent.getMovieSeq());
                req.setContent(movieReviewEvent.getContent());
                wordCloudEvents.add(req);
            });

            wordCloudUpdateService.updateWordCloud(wordCloudEvents);
        };
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<WordCloudDto>  wordCloudUpdateJdbcBatchItemWriter() {

        return new JdbcBatchItemWriterBuilder<WordCloudDto>()
                .dataSource(dataSource)
                .sql("UPDATE data_db.wordcloud_review_logs " +
                        "SET is_processed = 1 " +
                        "WHERE id = :id")
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public CompositeItemWriter<WordCloudDto> modelUpdatetCompositeItemWritercompositeItemWriter() {
        CompositeItemWriter<WordCloudDto> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(wordCloudUpdateItemWriter(wordCloudUpdateService), wordCloudUpdateJdbcBatchItemWriter()));
        return writer;
    }
}
