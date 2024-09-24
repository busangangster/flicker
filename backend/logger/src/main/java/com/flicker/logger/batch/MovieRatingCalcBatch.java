package com.flicker.logger.batch;

import com.flicker.logger.entity.MovieAverageRating;
import com.flicker.logger.repository.MovieAverageRatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MovieRatingCalcBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final ReviewAggregationService reviewAggregationService;
    private final MovieAverageRatingRepository movieAverageRatingRepository;

    @Bean
    public Job ratingCalcJob() {
        log.info("Movie rating calculation batch started");
        return new JobBuilder("calculateAverageRatingJob", jobRepository)
                .start(ratingStep())
                .build();
    }

    @Bean
    public Step ratingStep() {
        log.info("Movie rating calculation step started");

        return new StepBuilder("ratingStep", jobRepository)
                .<MovieAverageRating, MovieAverageRating>chunk(1000, platformTransactionManager)
                .reader(aggregatedMovieRatingReader())
                .processor(movieAverageRatingProcessor())
                .writer(movieAverageRatingWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<MovieAverageRating> aggregatedMovieRatingReader() {
        return new ItemReader<MovieAverageRating>() {
            private List<MovieAverageRating> aggregatedRatings;
            private int nextIndex = 0; // 현재 읽고 있는 데이터의 인덱스를 저장

            @Override
            public MovieAverageRating read() {
                // 리스트가 아직 초기화되지 않았다면 데이터를 가져옵니다.
                if (aggregatedRatings == null) {
                    aggregatedRatings = reviewAggregationService.aggregateMovieRatings();
                }

                // 리스트의 끝에 도달할 때까지 데이터를 하나씩 반환
                if (nextIndex < aggregatedRatings.size()) {
                    return aggregatedRatings.get(nextIndex++);
                } else {
                    return null; // 더 이상 데이터가 없으면 null 반환
                }
            }
        };
    }

    @Bean
    public ItemProcessor<MovieAverageRating, MovieAverageRating> movieAverageRatingProcessor() {
        return rating -> {
            return rating;
        };
    }

    @Bean
    public ItemWriter<MovieAverageRating> movieAverageRatingWriter() {
        return items -> {
            log.info("Saving aggregated movie ratings");
            movieAverageRatingRepository.saveAll(items); // 리스트 전체를 한 번에 저장
            log.info("Saved {} movie average ratings", items.size());
        };
    }
}
