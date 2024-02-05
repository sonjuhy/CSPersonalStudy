package com.study.CSStudy.api.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Component
@StepScope
public class MultiTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try{
            for(int i=0;i<10;i++){
                System.out.println(i);
                Thread.sleep(1000);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

//        List<Integer> totalList = new ArrayList<>();
//        for(int i=0;i<100;i++) totalList.add(i);
//
//        int divNum = 10;
//        int partitionSize = (int) Math.ceil((double) totalList.size() / divNum);
//        System.out.println("partition size : "+partitionSize);
//        List<List<Integer>> groups = IntStream.range(0, divNum)
//                .mapToObj(i -> totalList
//                        .subList(i * partitionSize,
//                                Math.min((i + 1) * partitionSize,
//                                        totalList.size()
//                                )
//                        )
//                )
//                .toList();
        return RepeatStatus.FINISHED;
    }
}
