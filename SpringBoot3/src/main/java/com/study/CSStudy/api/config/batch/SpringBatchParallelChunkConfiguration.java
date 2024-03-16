package com.study.CSStudy.api.config.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SpringBatchParallelChunkConfiguration {

    @Bean
    public Job sampleParallelChunkJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new JobBuilder("chunkParallelJob", jobRepository)
                .preventRestart()
                .start(splitFlow(jobRepository, platformTransactionManager))
                .next(chunkStep4(jobRepository, platformTransactionManager))
                .build()
                .build();
    }

    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    public Flow splitFlow(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(flow1(jobRepository, platformTransactionManager), flow2(jobRepository, platformTransactionManager))
                .build();
    }


    public Flow flow1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
     return new FlowBuilder<SimpleFlow>("parallelFlow1")
             .start(chunkStep1(jobRepository, platformTransactionManager))
             .next(chunkStep2(jobRepository, platformTransactionManager))
             .build();
    }

    public Flow flow2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new FlowBuilder<SimpleFlow>("parallelFlow2")
                .start(chunkStep3(jobRepository, platformTransactionManager))
                .build();
    }
    @Bean
    public Step chunkStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("chunkStep1", jobRepository)
                .<Integer, List<Integer>>chunk(10, platformTransactionManager)
                .reader(new ListItemReader<>(Arrays.asList(13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)))
                .writer(items->{
//                        Thread.sleep(1000);
                    log.info("chunk1 start");
                    Date date = new Date();
                    System.out.println("now : "+date.getTime());
                    System.out.println(items.getItems());

                })
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step chunkStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("chunkStep2", jobRepository)
                .<Integer, List<Integer>>chunk(10, platformTransactionManager)
                .reader(new ListItemReader<>(Arrays.asList(13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)))
                .writer(items->{
                    log.info("chunk2 start");
                    System.out.println(Arrays.toString(items.getItems().toArray()));
                })
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step chunkStep3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("chunkStep3", jobRepository)
                .<Integer, List<Integer>>chunk(10, platformTransactionManager)
                .reader(new ListItemReader<>(Arrays.asList(13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)))
                .writer(items->{
                    log.info("chunk3 start");
                    System.out.println(Arrays.toString(items.getItems().toArray()));
                })
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step chunkStep4(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("chunkStep4", jobRepository)
                .<Integer, List<Integer>>chunk(10, platformTransactionManager)
                .reader(new ListItemReader<>(Arrays.asList(13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)))
                .writer(items->{
                    log.info("chunk4 start");
                    System.out.println(Arrays.toString(items.getItems().toArray()));
                })
                .allowStartIfComplete(true)
                .build();
    }
}
