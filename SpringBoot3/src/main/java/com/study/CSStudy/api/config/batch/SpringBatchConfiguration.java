package com.study.CSStudy.api.config.batch;

import com.study.CSStudy.api.component.FirstTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
    public Job simpleJob1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("simpleJob", jobRepository)
//                .start(simpleStep1(jobRepository, platformTransactionManager))
//                    .on(ExitStatus.FAILED.getExitCode())// Failed 경우
//                    .to(simpleStep1Failed(jobRepository, platformTransactionManager)) // failedTasklet 으로 이동하여 실행
//                    .on("*") // failedTasklet 결과와 상관없이
//                    .end() // 종료
//                .from(simpleStep1(jobRepository, platformTransactionManager)) //simpleStep1 로부터
//                    .on("*") // 앞서 설정한 Failed 제외하고
//                    .to(simpleStep2(jobRepository, platformTransactionManager))// simpleStep2 실행
//                    .on("*") // simpleStep2 결과 상관 없이
//                    .end()// 종료
//                .end()
//                .build();
                .start(simpleStep1(jobRepository,platformTransactionManager))
                .next(simpleStep2(jobRepository, platformTransactionManager))
                .build();
    }
    @Bean
    public Step simpleStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep01", jobRepository)
                .tasklet(errorTasklet(), platformTransactionManager)
                .allowStartIfComplete(true) // 이미 실행이 완료된 step 도 다시 실행하도록 설정
                .build();
    }

    @Bean
    public Step simpleStep1Failed(JobRepository repository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep1Failed", repository)
                .tasklet(failedTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step simpleStep2(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep02", jobRepository)
                .tasklet(firstTasklet, platformTransactionManager)
                .allowStartIfComplete(true) // 이미 실행이 완료된 step 도 다시 실행하도록 설정
                .build();
    }

//    @Bean
//    public Tasklet testTasklet(){
//        return ((contribution, chunkContext) -> {
//            log.info(">>>>> This is Step1");
//            return RepeatStatus.FINISHED;
//        });
//    }

    @Bean
    public Tasklet errorTasklet(){
        return ((contribution, chunkContext) -> {
            contribution.setExitStatus(ExitStatus.FAILED);
            return RepeatStatus.FINISHED;
        });
    }

    @Bean
    public Tasklet failedTasklet(){
        return ((contribution, chunkContext) -> {
            log.info("failed");
            return RepeatStatus.FINISHED;
        });
    }
}
