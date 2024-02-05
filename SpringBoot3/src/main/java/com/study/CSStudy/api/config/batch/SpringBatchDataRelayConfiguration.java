package com.study.CSStudy.api.config.batch;

import com.study.CSStudy.api.component.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
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
    private final CustomItemWriterForRelay customItemWriterForRelay;
    private final CustomStep customStep;

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
    public Step relayStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("relayStep1", jobRepository)
                .tasklet(relayTask1(), platformTransactionManager)
                .allowStartIfComplete(true) // 이미 실행이 완료된 step 도 다시 실행하도록 설정
                .build();
    }

    @Bean
    public Step relayStep2(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("relayStep2", jobRepository)
                .tasklet(relayTask2(), platformTransactionManager)
                .allowStartIfComplete(true) // 이미 실행이 완료된 step 도 다시 실행하도록 설정
                .build();
    }

    @Bean
    public Tasklet relayTask1(){
        return ((contribution, chunkContext) -> {
            List<Integer> list = new ArrayList<>();
            for(int i=0;i<23;i++)list.add(i);
            chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("listData", list); // job 안에 step끼리 공유 가능
            chunkContext.getStepContext().getStepExecution().getExecutionContext().put("listData", list); //step 간 공유 불가능
            return RepeatStatus.FINISHED;
        });
    }

    @Bean
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

    @Bean
    public Step relayStep3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("relayStep3", jobRepository)
                .<Integer, List<Integer>>chunk(10, platformTransactionManager)
                .reader(new ListItemReader<>(Arrays.asList(13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)))
                .writer(items->{
                    System.out.println(items.getItems());
                })
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step relayStep4(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("relayStep4", jobRepository)
                .<Integer, List<Integer>>chunk(10, platformTransactionManager)
                .reader(customItemReaderForRelay)
                .writer(customItemWriterForRelay)
                .allowStartIfComplete(true)
                .build();
    }
}
