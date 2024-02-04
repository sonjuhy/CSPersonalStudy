package com.study.CSStudy.api.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @Autowired
    private Job sampleParallelJob;
    @Autowired
    private JobLauncher jobLauncher;

    @GetMapping("/jobRunning")
    public void running(){
        try{
            Date date = new Date();
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("publicCheck-"+date.getTime(), String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();
            jobLauncher.run(sampleParallelJob, jobParameters);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
}
