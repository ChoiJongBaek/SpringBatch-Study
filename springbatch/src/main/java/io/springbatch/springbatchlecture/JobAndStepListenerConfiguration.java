package io.springbatch.springbatchlecture;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class JobAndStepListenerConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomStepExecutionListener customStepExecutionListener;
    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .next(step2())
                .listener(new CustomJobExecutionListener())
//                .listener(new CustomAnnotationJobExecutionListener())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED)
                .listener(customStepExecutionListener)
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED)
                .listener(customStepExecutionListener)
                .build();
    }

}