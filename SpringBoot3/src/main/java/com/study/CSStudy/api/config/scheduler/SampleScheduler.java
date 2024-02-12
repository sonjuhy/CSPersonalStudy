package com.study.CSStudy.api.config.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@EnableScheduling
@Configuration
public class SampleScheduler {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job simpleJob1;

    @Scheduled(cron = "0 0 12 * * ?")
    public void runEvery12Clock() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        System.out.println("hello? batch start");
        Date date = new Date();
        JobParameters simpleJobParameters = new JobParametersBuilder()
                .addString("simpleCheck-"+date.getTime(), String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(simpleJob1, simpleJobParameters);
    }
}
