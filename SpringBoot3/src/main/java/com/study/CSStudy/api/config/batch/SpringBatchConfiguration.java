package com.study.CSStudy.api.config.batch;

import com.study.CSStudy.api.component.FirstTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SpringBatchConfiguration {

    private final FirstTasklet firstTasklet;
    @Bean
    public Job simpleJob1(JobRepository jobRepository, Step simpleStep1, Step simpleStep2) {
        return new JobBuilder("simpleJob", jobRepository)
                .start(simpleStep1)
                .next(simpleStep2)
                .build();
    }
    @Bean
    public Step simpleStep1(JobRepository jobRepository, Tasklet testTasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep1", jobRepository)
                .tasklet(testTasklet, platformTransactionManager).build();
    }

    @Bean
    public Step simpleStep2(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep2", jobRepository)
                .tasklet(firstTasklet, platformTransactionManager).build();
    }

    @Bean
    public Tasklet testTasklet(){
        return ((contribution, chunkContext) -> {
            log.info(">>>>> This is Step1");
            return RepeatStatus.FINISHED;
        });
    }
}
