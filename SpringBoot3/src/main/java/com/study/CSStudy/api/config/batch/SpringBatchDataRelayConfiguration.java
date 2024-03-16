package com.study.CSStudy.api.config.batch;

import com.study.CSStudy.api.component.batchComponent.CustomItemReaderForRelay;
import com.study.CSStudy.api.component.batchComponent.CustomItemWriterForRelay;
import com.study.CSStudy.api.component.batchComponent.CustomStep;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SpringBatchDataRelayConfiguration {

    private final CustomItemReaderForRelay customItemReaderForRelay;
    private final CustomItemWriterForRelay customItemWriterForRelay,customItemWriterForRelay1,customItemWriterForRelay2;

    @Bean
    public Job relayJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new JobBuilder("JobForDataRelayTest", jobRepository)
                .start(relayStep1(jobRepository, platformTransactionManager))
                .next(relayStep2(jobRepository, platformTransactionManager))
//                .next(customStep)
//                .next(relayStep3(jobRepository, platformTransactionManager))
//                .next(relayStep4(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Job relayChunkJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new JobBuilder("JobForDataRelayTestWithChunk", jobRepository)
                .start(relayChunkStep1(jobRepository, platformTransactionManager))
                .next(relayChunkStep2(jobRepository, platformTransactionManager))
                .build();
    }

    public Step relayStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("relayStep1", jobRepository)
                .tasklet(relayTask1(), platformTransactionManager)
                .allowStartIfComplete(true) // 이미 실행이 완료된 step 도 다시 실행하도록 설정
                .build();
    }

    public Step relayStep2(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("relayStep2", jobRepository)
                .tasklet(relayTask2(), platformTransactionManager)
                .allowStartIfComplete(true) // 이미 실행이 완료된 step 도 다시 실행하도록 설정
                .build();
    }

    public Tasklet relayTask1(){
        return ((contribution, chunkContext) -> {
            List<Integer> list = new ArrayList<>();
            for(int i=0;i<23;i++)list.add(i);
            chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("listData", list); // job 안에 step끼리 공유 가능
            chunkContext.getStepContext().getStepExecution().getExecutionContext().put("listData", list); //step 간 공유 불가능
            return RepeatStatus.FINISHED;
        });
    }

    public Tasklet relayTask2(){
        return ((contribution, chunkContext) -> {
            List<Integer> list = (List<Integer>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("listData");
            if(list == null)
                System.out.println("data is null");
            else {
                try {
                    Thread.sleep(3000);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                System.out.println(list);
            }
            return RepeatStatus.FINISHED;
        });
    }

    public Step relayChunkStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("relayChunkStep1", jobRepository)
                .<Integer, Integer>chunk(10, platformTransactionManager)
                .reader(customItemReaderForRelay)
                .writer(customItemWriterForRelay)
                .allowStartIfComplete(true)
                .build();
    }

    public Step relayChunkStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("relayChunkStep2", jobRepository)
                .<Integer, Integer>chunk(10, platformTransactionManager)
                .reader(customItemReaderForRelay)
                .writer(compositeItemWriter())
                .allowStartIfComplete(true)
                .build();
    }

    public CompositeItemWriter<Integer> compositeItemWriter(){
        CompositeItemWriter<Integer> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList(customItemWriterForRelay, customItemWriterForRelay1, customItemWriterForRelay2));
        return compositeItemWriter;
    }

}
