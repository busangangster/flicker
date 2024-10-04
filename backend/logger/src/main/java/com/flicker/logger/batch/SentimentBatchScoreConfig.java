package com.flicker.logger.batch;

import com.flicker.logger.config.KafkaItemWriter;
import com.flicker.logger.dto.SentimentResult;
import com.flicker.logger.dto.SentimentReview;
import com.flicker.logger.dto.SentimentReviewEvent;
import com.flicker.logger.application.SentimentAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SentimentBatchScoreConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SentimentAnalysisService sentimentAnalysisService;
    @Qualifier("dataDBSource") private final DataSource dataSource;

    @Bean
    public Job sentimentBatchScoreJob() {

        log.info("Sentiment batch score job started");

        return new JobBuilder("SentimentScoreJob", jobRepository)
                .start(sentimentBatchScoreStep())
                .next(sendToSentimentStep())
                .build();
    }

    @Bean
    public Step sentimentBatchScoreStep() {

        log.info("Sentiment batch score step started");

        return new StepBuilder("SentimentBatchScoreStep", jobRepository)
                .<SentimentReviewEvent, SentimentReviewEvent> chunk(1000, transactionManager)
                .reader(sentimentBatchScoreReader())
            .processor(sentimentBatchScoreProcessor())
            .writer(sentimentScoreWriter(sentimentAnalysisService, jdbcBatchItemWriter(dataSource)))
            .build();
    }

    @Bean
    public Step sendToSentimentStep() {

        log.info("Sentiment sendToKafka batch score step started");

        return new StepBuilder("sendSentimentStep", jobRepository)
                .<SentimentReview, SentimentResult> chunk(10, transactionManager)
                .reader(sentimentBatchKafkaReader())
                .processor(sentimentBatchKafkaProcessor())
                .writer(sentimentResultCompositeItemWritercompositeItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<SentimentReviewEvent> sentimentBatchScoreReader() {

        return new JdbcCursorItemReaderBuilder<SentimentReviewEvent>()
                .dataSource(dataSource)
                .name("sentimentReader")
                .sql("SELECT review_seq, content " +
                        "FROM data_db.sentiment_review_logs " +
                        "WHERE is_processed = 0")
                .rowMapper(new BeanPropertyRowMapper<>(SentimentReviewEvent.class))
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<SentimentReview> sentimentBatchKafkaReader() {

        return new JdbcCursorItemReaderBuilder<SentimentReview>()
                .dataSource(dataSource)
                .name("sentimentReader")
                .sql("SELECT review_seq, sentiment_score " +
                        "FROM data_db.sentiment_review_logs " +
                        "WHERE is_processed = 1 " +
                        "and is_delete = 0")
                .rowMapper(new BeanPropertyRowMapper<>(SentimentReview.class))
                .build();
    }


    @Bean
    @StepScope
    public ItemProcessor<SentimentReviewEvent, SentimentReviewEvent> sentimentBatchScoreProcessor() {

        return items -> {
            // 여기서 감성분석 모델을 분석 API를 호춣하는 코드
            // score <- API 호출
            // 테스트를 위해 0.5점 주입
//            Double score = 0.5;
//
//            return SentimentResult.builder()
//                    .reviewSeq(item.getReviewSeq())
//                    .sentimentScore(score)
//                    .build();
            return items;
        };
    }

    @Bean
    @StepScope
    public ItemProcessor<SentimentReview, SentimentResult> sentimentBatchKafkaProcessor() {

        return item -> SentimentResult.builder()
                .reviewSeq(item.getReviewSeq())
                .sentimentScore(item.getSentimentScore())
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<SentimentResult> jdbcBatchItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<SentimentResult>()
                .dataSource(dataSource)
                .sql("UPDATE data_db.sentiment_review_logs " +
                        "SET sentiment_score = :sentimentScore, " +
                        "is_processed = 1, " +
                        "updated_at = CURRENT_TIMESTAMP " +  // 현재 시간 설정
                        "WHERE review_seq = :reviewSeq")
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<SentimentReviewEvent> sentimentScoreWriter(SentimentAnalysisService sentimentAnalysisService, JdbcBatchItemWriter<SentimentResult> jdbcBatchItemWriter) {
        return items -> {
            List<SentimentReviewEvent> reviewEventList = new ArrayList<>();
            items.forEach(sentimentReviewEvent -> {
                SentimentReviewEvent reviewEvent = new SentimentReviewEvent();
                reviewEvent.setReviewSeq(sentimentReviewEvent.getReviewSeq());
                reviewEvent.setContent(sentimentReviewEvent.getContent());
                reviewEventList.add(reviewEvent);
            });
            List<SentimentResult> results = sentimentAnalysisService.batchAnalyze(reviewEventList);

            // 감정 분석 결과를 DB에 저장하기 위해 Chunk로 변환
            Chunk<SentimentResult> chunk = new Chunk<>(results);

            jdbcBatchItemWriter.write(chunk);
        };
    }

//    @Bean
//    @StepScope
//    public ItemWriter<SentimentResult> sentimentKafkaSendWriter(KafkaTemplate<String, String> kafkaTemplate) {
//
//        return item -> {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule());
//            String message = objectMapper.writeValueAsString(item);
//
//            kafkaTemplate.send("sentiment-result", message);
//            log.info("Movie rating sent to kafka : {}", message);
//        };
//    }

    @Bean
    @StepScope
    public KafkaItemWriter<SentimentResult> sentimentKafkaSendWriter(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaItemWriter<>(kafkaTemplate, "sentiment-result");
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<SentimentResult> sentimentKafkaWriter() {

        return new JdbcBatchItemWriterBuilder<SentimentResult>()
                .dataSource(dataSource)
                .sql("UPDATE data_db.sentiment_review_logs " +
                        "SET is_delete = 1 " +
                        "WHERE review_seq = :reviewSeq")
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public CompositeItemWriter<SentimentResult> sentimentResultCompositeItemWritercompositeItemWriter() {
        CompositeItemWriter<SentimentResult> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(sentimentKafkaSendWriter(kafkaTemplate), sentimentKafkaWriter()));
        return writer;
    }

}
