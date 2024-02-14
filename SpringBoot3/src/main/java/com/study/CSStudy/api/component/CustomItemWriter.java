package com.study.CSStudy.api.component;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomItemWriter implements ItemWriter<Integer> {

    private StepExecution stepExecution;

    @Override
    public void write(Chunk<? extends Integer> chunk) throws Exception {
        List<Integer> list = new ArrayList<>();
        for(int i=13;i<29;i++) list.add(i);
        ExecutionContext stepContext = this.stepExecution.getExecutionContext();
        stepContext.put("listData", list);
        System.out.println("CustomItemWriter list size : "+list.size());
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution){
        this.stepExecution = stepExecution;
    }
}
