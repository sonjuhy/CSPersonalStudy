package com.study.CSStudy.api.config.batch;

import com.study.CSStudy.api.component.CustomItemReader;
import com.study.CSStudy.api.component.MultiTasklet;
import com.study.CSStudy.api.component.SubMultiTasklet;
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
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SpringBatchParallelConfiguration {


    /*
     * batch parallel steps
     * */

    private final MultiTasklet multiTasklet;
    private final SubMultiTasklet subMultiTasklet;
    private final CustomItemReader customItemReader;

    @Bean
    public Job sampleParallelJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new JobBuilder("parallelJob", jobRepository)
                .start(splitFlow(jobRepository, platformTransactionManager))
//                .next();
                .build()
                .build();
    }

    @Bean
    public TaskExecutor parallelTaskExecutor(){
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean
    public Flow splitFlow(JobRepository repository, PlatformTransactionManager platformTransactionManager){
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(parallelTaskExecutor())
                .add(
                        flow1(repository, platformTransactionManager),
                        flow2(repository, platformTransactionManager)
                )
                .next(flow3(repository, platformTransactionManager))
                .build();
    }

    @Bean
    public Flow flow1(JobRepository repository, PlatformTransactionManager platformTransactionManager) {
        return new FlowBuilder<SimpleFlow>("flow1")
                .start(step1(repository, platformTransactionManager))
                .build();
    }

    @Bean
    public Flow flow2(JobRepository repository, PlatformTransactionManager platformTransactionManager) {
        return new FlowBuilder<SimpleFlow>("flow2")
                .start(step2(repository, platformTransactionManager))
                .build();
    }

    @Bean
    public Flow flow3(JobRepository repository, PlatformTransactionManager platformTransactionManager) {
        return new FlowBuilder<SimpleFlow>("flow3")
                .start(step3(repository, platformTransactionManager))
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step1", jobRepository)
                .tasklet(multiTasklet, platformTransactionManager)
                .allowStartIfComplete(true) // 이미 실행이 완료된 step 도 다시 실행하도록 설정
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step2", jobRepository)
                .tasklet(subMultiTasklet, platformTransactionManager)
                .allowStartIfComplete(true) // 이미 실행이 완료된 step 도 다시 실행하도록 설정
                .build();
    }

    @Bean
    public Step step3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step3-custom R/W", jobRepository)
                .<Integer, Integer>chunk(10, platformTransactionManager)
                .reader(customItemReader)
                .writer(customWriter())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public ItemWriter<Integer> customWriter(){
        return items -> {
            try {
                Thread.sleep(3000);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            for(int num : items){
                System.out.println(num);
            }
        };
    }
}
