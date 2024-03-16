package com.study.CSStudy.api.config.batch;

import com.study.CSStudy.api.component.batchComponent.*;
import com.study.CSStudy.api.component.batchComponent.Mapper.CustomFileInfoRowMapper;
import com.study.CSStudy.api.dto.FileDto;
import com.study.CSStudy.db.entity.FileEntity;
import com.study.CSStudy.studySpring.db.repository.FileRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SpringBatchMultiThreadConfiguration {


    /*
     * batch Multi-Thread steps
     * */
    private final int WORKER_SIZE = 3;

    @Autowired
    FileRepository fileRepository;

    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    private final MultiTasklet multiTasklet;
    private final SubMultiTasklet subMultiTasklet;
    private final CustomItemReaderForMultiThread customItemReaderForMultiThread;
    private final CustomItemWriterForMultiThread customItemWriterForMultiThread;

    @Bean
    public Job sampleMultiThreadJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new JobBuilder("multiThreadJob", jobRepository)
                .start(multiThreadStep(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Job sampleMultiThreadFileToDBJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new JobBuilder("multiThreadFileToDBJob", jobRepository)
                .start(multiThreadStepFileToDB(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Job sampleMultiThreadReadFromDBJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) throws Exception {
        return new JobBuilder("multiThreadReadFromDBJob", jobRepository)
                .start(multiThreadStepReadFromDB(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public TaskExecutor multiThreadTaskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(WORKER_SIZE);
        taskExecutor.setMaxPoolSize(WORKER_SIZE);
        taskExecutor.setThreadNamePrefix("multiThreadExecutor-");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
//        taskExecutor.setAllowCoreThreadTimeOut(true);
//        taskExecutor.setKeepAliveSeconds(1);
        taskExecutor.initialize();
        return taskExecutor;

//        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    public Step multiThreadStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("multiThreadStep", jobRepository)
                .<Integer, Integer>chunk(10, platformTransactionManager)
                .reader(customItemReaderForMultiThread)
                .writer(customItemWriterForMultiThread)
                .taskExecutor(multiThreadTaskExecutor())
                .build();
    }

    public Step multiThreadStepFileToDB(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("multiThreadStepFileToDB", jobRepository)
                .<FileDto, FileDto>chunk(1000, platformTransactionManager)
                .reader(flatFileItemReader())
                .writer(jdbcBatchItemWriter())
                .taskExecutor(multiThreadTaskExecutor())
                .listener(jobExecutionListener(multiThreadTaskExecutor()))
                .build();
    }

    public Step multiThreadStepReadFromDB(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        PagingQueryProvider pagingQueryProvider = pagingQueryProvider().getObject();
        return new StepBuilder("multiThreadStepReadFromDB", jobRepository)
                .<FileEntity, FileEntity>chunk(100000, platformTransactionManager)
                .reader(repositoryItemReader())
                .writer(items->{
                    log.info("item size : "+items.size());
                    log.info("first item : "+items.getItems().get(0));
                })
                .build();
    }

    public FlatFileItemReader<FileDto> flatFileItemReader(){
        log.info("reader - {}",Thread.currentThread().getName());
        return new FlatFileItemReaderBuilder<FileDto>()
                .name("txtItemReader")
                .resource(new PathResource("E:\\WorkSpace\\FileList.txt"))
                .delimited().delimiter(" ")
                .names("id","name","size")
                .fieldSetMapper(fieldSet -> {
                    FileDto dto = new FileDto();
                    dto.setId(Integer.parseInt(fieldSet.readString(0)));
                    dto.setName(fieldSet.readString(1));
                    dto.setSize(Integer.parseInt(fieldSet.readString(2)));
                    return dto;
                })
                .build();
    }
    public JdbcBatchItemWriter<FileDto> jdbcBatchItemWriter(){
        log.info("writer start");
        JdbcBatchItemWriter<FileDto> jdbcBatchItemWriter = new JdbcBatchItemWriterBuilder<FileDto>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO file(file_size, id, name) VALUES(:size, :id, :name)")
                .build();
        jdbcBatchItemWriter.afterPropertiesSet();
        return jdbcBatchItemWriter;
    }

    public JpaPagingItemReader<FileEntity> jpaPagingItemReader(){
        return new JpaPagingItemReaderBuilder<FileEntity>()
                .name("jpaPageReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT f FROM file f")
                .pageSize(1000)
                .build();
    }

    public RepositoryItemReader<FileEntity> repositoryItemReader(){
        RepositoryItemReader<FileEntity> repositoryItemReader = new RepositoryItemReader<>();
        repositoryItemReader.setRepository(fileRepository);
        repositoryItemReader.setMethodName("findAll");
        repositoryItemReader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        repositoryItemReader.setPageSize(100000);

        return repositoryItemReader;
    }



    public JdbcPagingItemReader<FileDto> jdbcPagingItemReader(PagingQueryProvider pagingQueryProvider) {
        CustomFileInfoRowMapper customFileInfoRowMapper = new CustomFileInfoRowMapper();

        return new JdbcPagingItemReaderBuilder<FileDto>()
                .pageSize(1000)
                .fetchSize(1000)
                .dataSource(dataSource)
                .queryProvider(pagingQueryProvider)
                .rowMapper(customFileInfoRowMapper)
                .name("jdbcItemReader")
                .build();
    }

    public SqlPagingQueryProviderFactoryBean pagingQueryProvider() {
        log.info("dataSource : "+(dataSource == null ? "null" : "not null"));
        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
        queryProviderFactoryBean.setDataSource(dataSource);
//
//        queryProviderFactoryBean.setSelectClause("id, name, file_size");
//        queryProviderFactoryBean.setFromClause("from file");
////        queryProviderFactoryBean.setWhereClause("조건문");
//
//        Map<String, Order> sortKeys = new HashMap<>();
//
//        sortKeys.put("id", Order.ASCENDING);
//
//        queryProviderFactoryBean.setSortKeys(sortKeys);
//        return queryProviderFactoryBean.getObject();

        queryProviderFactoryBean.setSelectClause("SELECT id, name, file_size");
        queryProviderFactoryBean.setFromClause("FROM file");
        queryProviderFactoryBean.setSortKey("id");
        return queryProviderFactoryBean;
    }
    public JobExecutionListener jobExecutionListener(TaskExecutor taskExecutor){
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("job start");
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                ((ThreadPoolTaskExecutor)taskExecutor).shutdown();
            }
        };
    }
}
