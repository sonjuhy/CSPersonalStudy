package com.study.CSStudy.api.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
public class SubMultiTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        try{
            for(int i=10;i<20;i++){
                System.out.println(i);
                Thread.sleep(1000);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return RepeatStatus.FINISHED;
    }
}
