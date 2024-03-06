package com.study.CSStudy;

import com.study.CSStudy.api.config.batch.SpringBatchConfiguration;
import com.study.CSStudy.api.config.batch.SpringBatchParallelChunkConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class CsStudyApplicationTests {

	@Autowired
	private Job simpleChunkJob1;
	@Autowired
	private Job simpleChunkJob2;
	@Autowired
	private Job simpleChunkJob3;
	@Autowired
	private Job sampleParallelChunkJob;
	@Autowired
	private JobLauncher jobLauncher;

	@Test
	void contextLoads() {
	}

	@Test
	void chunkTest() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
		Date date = new Date();
		JobParameters simpleJobParameters = new JobParametersBuilder()
				.addString("simpleCheck-"+date.getTime(), String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(sampleParallelChunkJob, simpleJobParameters);
	}
	@Test
	void batchTest() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
		Date date = new Date();
		JobParameters simpleJobParameters = new JobParametersBuilder()
				.addString("simpleCheck-"+date.getTime(), String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(simpleChunkJob1, simpleJobParameters);
	}
	@Test
	void batchTest2() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
		Date date = new Date();
		JobParameters simpleJobParameters = new JobParametersBuilder()
				.addString("simpleCheck-"+date.getTime(), String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(simpleChunkJob2, simpleJobParameters);
	}

	@Test
	void batchTest3() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
		Date date = new Date();
		JobParameters simpleJobParameters = new JobParametersBuilder()
				.addString("simpleCheck-"+date.getTime(), String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(simpleChunkJob3, simpleJobParameters);
	}
}
