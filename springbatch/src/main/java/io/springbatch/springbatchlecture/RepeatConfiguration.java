package io.springbatch.springbatchlecture;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class RepeatConfiguration {

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
                .reader(new ItemReader<String>() {

                    int i = 0;

                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i++;
                        return i>3?null:"item" + i;
                    }
                })
                .processor(new ItemProcessor<String, String>() {

                    RepeatTemplate repeatTemplate = new RepeatTemplate();

                    @Override
                    public String process(String item) throws Exception {
//                        //3번의 반복 후 종료
//                        repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(3));
//                        //3초동안 반복 후 3초가 지나면 반복이 종료
//                        repeatTemplate.setCompletionPolicy(new TimeoutTerminationPolicy(3000));
//
//                        //여러 개를 사용하되 먼저 조합에 부합되면 종료
//                        CompositeCompletionPolicy completionPolicy = new CompositeCompletionPolicy();
//                        CompletionPolicy[] completionPolicies = new CompletionPolicy[]{
//                                new SimpleCompletionPolicy(3),
//                                new TimeoutTerminationPolicy(3000)};
//                        completionPolicy.setPolicies(completionPolicies);
//                        repeatTemplate.setCompletionPolicy(completionPolicy);

                        //3번의 예외까지는 반복이 종료되지 않고 3번이 넘어가면 반복이 종료
                        repeatTemplate.setExceptionHandler(simpleLimitExceptionHandler());

                        repeatTemplate.iterate(new RepeatCallback() {
                            @Override
                            public RepeatStatus doInIteration(RepeatContext context) throws Exception {

                                System.out.println("repeatTemplate is testing");
                                throw new RuntimeException("Exception is occurred");

//                                return RepeatStatus.CONTINUABLE;
                            }
                        });

                        return item;
                    }
                })
                .writer(items -> System.out.println(items))
                .build();
    }

    @Bean
    public ExceptionHandler simpleLimitExceptionHandler() {

        return new SimpleLimitExceptionHandler(3);

    }
}