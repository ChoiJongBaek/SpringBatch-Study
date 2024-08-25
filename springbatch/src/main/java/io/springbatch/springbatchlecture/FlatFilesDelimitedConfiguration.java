package io.springbatch.springbatchlecture;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class FlatFilesDelimitedConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        return this.jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(10)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public FlatFileItemWriter<? super Customer> customItemWriter() {

        return new FlatFileItemWriterBuilder<>()
                .name("flatFileWriter")
                .resource(new FileSystemResource("/Users/jongbaek/Desktop/study/SpringBatch/source/springbatch/src/main/resources/customer.txt"))
                // 기존 데이터 이후에 계속 데이터를 작성할 지 여부 확인
                .append(true)
                // 쓰기 작업 할 데이터가 없으면 파일 삭제 여부 확인
                .shouldDeleteIfEmpty(true)
                .delimited()
                .delimiter("|")
                .names(new String[]{"id", "name", "age"})
                .build();
    }

    @Bean
    public ListItemReader customItemReader() {
        List<Customer> customers = Arrays.asList(new Customer(1, "hong gil dong1", 41),
                new Customer(2, "hong gil dong2", 42),
                new Customer(3, "hong gil dong3", 43));

        ListItemReader<Customer> reader = new ListItemReader<>(customers);

        return reader;
    }
}