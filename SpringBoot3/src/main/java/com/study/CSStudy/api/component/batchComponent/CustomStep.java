package com.study.CSStudy.api.component.batchComponent;

import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomStep implements Step {
    private final static String stepName = "CustomStep";
    @Override
    public String getName() {
        return stepName;
    }

    @Override
    public boolean isAllowStartIfComplete() {
//        return Step.super.isAllowStartIfComplete();
        return true;
    }

    @Override
    public void execute(StepExecution stepExecution) throws JobInterruptedException {
        System.out.println("CustomStep execute");
        List<Integer> list = (List<Integer>) stepExecution.getJobExecution().getExecutionContext().get("listData");
        System.out.println(list);
    }
}
