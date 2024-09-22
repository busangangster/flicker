package com.flicker.logger.batch;

import com.flicker.logger.entity.AfterEntity;
import com.flicker.logger.repository.AfterRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class ThirdBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final AfterRepository afterRepository;

    @Bean
    public Job thirdBatchJob() {

        System.out.println("Third Batch Job");

        return new JobBuilder("thirdJob", jobRepository)
                .start(thirdStep())
                .build();
    }

    public Step thirdStep() {

        return new StepBuilder("thirdStep", jobRepository)
                .<Row, AfterEntity>chunk(10, platformTransactionManager)
                .reader(excelReader())
                .processor(thirdProcessor())
                .writer(thirdWriter())
                .build();
    }

    public ItemStreamReader<Row> excelReader() {

        try {
            return new ExcelRowReader("/Users/choejaeyong/Desktop/S11P21E206/backend/logger/src/main/resources/file/sample.xlsx");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ItemProcessor<Row, AfterEntity> thirdProcessor() {

        return item -> {

            AfterEntity afterEntity = new AfterEntity();
            afterEntity.setUsername(item.getCell(0).getStringCellValue());

            return afterEntity;
        };
    }

    public RepositoryItemWriter<AfterEntity> thirdWriter() {

        return new RepositoryItemWriterBuilder<AfterEntity>()
                .repository(afterRepository)
                .methodName("save")
                .build();
    }

}
