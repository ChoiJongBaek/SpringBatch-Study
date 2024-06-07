package io.springbatch.springbatchlecture;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class BatchStatusExitStatusConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

//    @Bean
//    public Job parentJob() {
//        return this.jobBuilderFactory.get("batchJob")
//                .start(step1())
//                .next(step2())
//                .build();
//    }

    // flowJob
    @Bean
    public Job parentJob() {
        return this.jobBuilderFactory.get("batchJob")
                .start(step1())
                // step1의 상태가 FAILED일 경우 step2 실행
                .on("FAILED")
                .to(step2())
                .end()
                .build();
    }
    
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step1 has executed");
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step2 has executed");
                    // BATCH_STEP EXECUTION
                    // EXIT_CODE -> FAILED BATCH_STATUS -> COMPLETE
                    // BATCH_JOB_EXECUTION
                    // EXIT_CODE -> FAILED(마지막 실행한 step의 EXIT_CODE 상태를 따라간다), BATCH_STATUS -> COMPLETE
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
