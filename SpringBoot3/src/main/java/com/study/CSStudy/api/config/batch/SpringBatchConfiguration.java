package com.study.CSStudy.api.config.batch;

import com.study.CSStudy.api.component.batchComponent.CustomItemReader;
import com.study.CSStudy.api.component.batchComponent.CustomItemWriter;
import com.study.CSStudy.api.component.batchComponent.FirstTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SpringBatchConfiguration {

    private final FirstTasklet firstTasklet;

    private final CustomItemReader customItemReader;
    private final CustomItemWriter customItemWriter;

    @Bean
    public Job simpleJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("simpleJob", jobRepository)
                .start(simpleStep1(jobRepository, platformTransactionManager))
                    .on(ExitStatus.FAILED.getExitCode())// Failed 경우
                    .to(simpleStep1Failed(jobRepository, platformTransactionManager)) // failedTasklet 으로 이동하여 실행
                    .on("*") // failedTasklet 결과와 상관없이
                    .end() // 종료
                .from(simpleStep1(jobRepository, platformTransactionManager)) //simpleStep1 로부터
                    .on("*") // 앞서 설정한 Failed 제외하고
                    .to(simpleStep2(jobRepository, platformTransactionManager))// simpleStep2 실행
                    .on("*") // simpleStep2 결과 상관 없이
                    .end()// 종료
                .end()
                .build();
    }

    @Bean
    public Job simpleJob1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("simpleJob1", jobRepository)
                .start(simpleStep1(jobRepository,platformTransactionManager))
                .next(simpleStep2(jobRepository, platformTransactionManager))
                .build();
    }
    @Bean
    public Job simpleChunkJob1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("simpleChunkJob1", jobRepository)
                .preventRestart()
                .start(simpleStepWithItemReaderAndWriter(jobRepository,platformTransactionManager))
                .build();
    }

    /*
    * Test for reader, writer method is out of step
    * */
    @Bean
    public Job simpleChunkJob2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("simpleChunkJob2", jobRepository)
                .preventRestart()
                .start(simpleStepWithItemOutSideReaderAndWriter(jobRepository,platformTransactionManager))
                .build();
    }

    /*
    * Test for reader, writer impl class
    * */
    @Bean
    public Job simpleChunkJob3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("simpleChunkJob3", jobRepository)
                .start(simpleStepWithItemReaderAndWriterClass(jobRepository,platformTransactionManager))
                .build();
    }

    public Step simpleStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep01", jobRepository)
                .tasklet(errorTasklet(), platformTransactionManager)
                .allowStartIfComplete(true) // 이미 실행이 완료된 step 도 다시 실행하도록 설정
                .build();
    }

    public Step simpleStep1Failed(JobRepository repository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep1Failed", repository)
                .tasklet(failedTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    public Step simpleStep2(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep02", jobRepository)
                .tasklet(firstTasklet, platformTransactionManager)
                .allowStartIfComplete(true) // 이미 실행이 완료된 step 도 다시 실행하도록 설정
                .build();
    }

    public Step simpleStepWithItemReaderAndWriter(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStepWithItemReaderAndWriter", jobRepository)
                .<String, String>chunk(2, platformTransactionManager) // 첫번째 String : reader, 두번째 String : writer 의 파라미터 값
                .reader(new ListItemReader<>(Arrays.asList("sample data1","sample data2","sample data3","sample data4")))
                .writer(items->{
                    log.info("SimpleStepWithItemReaderAndWriter write items size : " + items.size());
                    for (String item : items) {
                        log.info("SimpleStepWithItemReaderAndWriter write - item : " + item);
                    }
                })
                .build();
    }

    public Step simpleStepWithItemOutSideReaderAndWriter(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStepWithItemOutSideReaderAndWriter", jobRepository)
                .<String, String>chunk(2, platformTransactionManager) // 첫번째 String : reader, 두번째 String : writer 의 파라미터 값
                .reader(simpleItemReader())
                .writer(this::simpleItemWriter)
                .build();
    }

    public Step simpleStepWithItemReaderAndWriterClass(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStepWithItemReaderAndWriterClass", jobRepository)
                .<String, String>chunk(2, platformTransactionManager) // 첫번째 String : reader, 두번째 String : writer 의 파라미터 값
                .reader(customItemReader)
                .writer(customItemWriter)
                .build();
    }

//    @Bean
//    public Tasklet testTasklet(){
//        return ((contribution, chunkContext) -> {
//            log.info(">>>>> This is Step1");
//            return RepeatStatus.FINISHED;
//        });
//    }

    public Tasklet errorTasklet(){
        return ((contribution, chunkContext) -> {
            contribution.setExitStatus(ExitStatus.FAILED);
            return RepeatStatus.FINISHED;
        });
    }

    public Tasklet failedTasklet(){
        return ((contribution, chunkContext) -> {
            log.info("failed");
            return RepeatStatus.FINISHED;
        });
    }


    public ItemReader<String> simpleItemReader(){

        List<String> data = new ArrayList<>();
        data.add("Sample Data 1");
        data.add("Sample Data 2");
        data.add("Sample Data 3");
        data.add("Sample Data 4");

        ItemReader<String> items = new ListItemReader<>(data);
        return items;
    }


    public void simpleItemWriter(Chunk<? extends String> chunk){
        log.info("simpleItemWriter start");
        for(String item : chunk.getItems()){
            log.info("item : "+item);
        }
    }

}
