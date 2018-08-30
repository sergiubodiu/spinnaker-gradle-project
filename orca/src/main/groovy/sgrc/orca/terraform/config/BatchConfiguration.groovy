package sgrc.orca.terraform.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepExecutionListener
import org.springframework.batch.core.configuration.ListableJobLocator
import org.springframework.batch.core.configuration.annotation.*
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar
import org.springframework.batch.core.configuration.support.JobLoader
import org.springframework.batch.core.converter.DefaultJobParametersConverter
import org.springframework.batch.core.converter.JobParametersConverter
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.JobOperator
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.launch.support.SimpleJobOperator
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.io.ClassPathResource
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import sgrc.orca.terraform.MdcThreadPoolTaskExecutor
import sgrc.orca.terraform.jobs.Instance
import sgrc.orca.terraform.jobs.InstanceItemProcessor
import sgrc.orca.terraform.log.LoggingListener

import javax.sql.DataSource

@Order()
@Configuration
@EnableBatchProcessing
class BatchConfiguration {

  @Bean
  AutomaticJobRegistrar registrar(JobLoader jobLoader) {
    AutomaticJobRegistrar registrar = new AutomaticJobRegistrar()
    registrar.setJobLoader(jobLoader)
    return registrar
  }

  @Bean
  BatchConfigurer configurer(DataSource dataSource){
    return new DefaultBatchConfigurer(dataSource)
  }

  @Bean
  @ConditionalOnMissingBean(JobOperator.class)
  SimpleJobOperator jobOperator(JobExplorer jobExplorer, JobLauncher jobLauncher,
                                       ListableJobLocator jobRegistry, JobRepository jobRepository)
      throws Exception {
    SimpleJobOperator factory = new SimpleJobOperator()
    factory.setJobExplorer(jobExplorer)
    factory.setJobLauncher(jobLauncher)
    factory.setJobRegistry(jobRegistry)
    factory.setJobRepository(jobRepository)
    factory.setJobParametersConverter(new DefaultJobParametersConverter())
    return factory
  }

  @Bean
  FlatFileItemReader<Instance> reader() {
    String[] names = ["name"]
    return new FlatFileItemReaderBuilder<Instance>()
        .name("instanceItemReader")
        .resource(new ClassPathResource("sample-data.csv"))
        .delimited()
        .names(names)
        .fieldSetMapper(new BeanWrapperFieldSetMapper<Instance>() {{
      setTargetType(Instance.class)
    }})
        .build()
  }

  @Bean
  InstanceItemProcessor processor() {
    return new InstanceItemProcessor()
  }

  @Bean
  JdbcBatchItemWriter<Instance> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Instance>()
        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
        .sql("INSERT INTO instance (name) VALUES (:name)")
        .dataSource(dataSource)
        .build()
  }

  @Bean
  JobCompletionNotificationListener jobCompletionNotificationListener() {
    return new JobCompletionNotificationListener()
  }

  @Bean
  Step step1(StepBuilderFactory steps, LoggingListener listener, JdbcBatchItemWriter<Instance> writer) {
    StepExecutionListener stepExecutionListener = listener;
    return steps.get("step1")
        .<Instance, Instance> chunk(10)
        .reader(reader())
        .processor(processor())
        .writer(writer)
        .faultTolerant()
        .retry(Exception.class)
        .retryLimit(3)
        .listener(stepExecutionListener)
        .build()
  }

  @Bean
  Job importUserJob(JobBuilderFactory jobs,
                    StepBuilderFactory steps,
                    JobCompletionNotificationListener jobCompletionNotificationListener,
                    Packer packer,
                    Terraform terraform) {
    LoggingListener loggingListener = new LoggingListener()

    Step terraformStep = steps.get("file-db")
        .<Student, Student>chunk(100)
        .reader(step1.fileReader(null))
        .writer(step1.jdbcWriter(null))
        .build();

    Step packerStep = steps.get("db-file")
        .<Map<Integer, Integer>, Map<Integer, Integer>>chunk(100)
        .reader(step2.jdbcReader(null))
        .writer(step2.fileWriter(null))
        .build();

    return jobs.get("importUserJob")
        .incrementer(new RunIdIncrementer())
        .listener(jobCompletionNotificationListener)
        .listener(loggingListener)
        .start(packerStep)
        .next(terraformStep)
        .build()
  }

  @Bean
  TaskExecutor taskExecutor(BatchConfigurationProperties batchConfig) {
    ThreadPoolTaskExecutor taskExecutor = new MdcThreadPoolTaskExecutor()
    taskExecutor.setCorePoolSize(batchConfig.getTaskExecutor().getCorePoolSize())
    taskExecutor.setQueueCapacity(batchConfig.getTaskExecutor().getQueueCapacity())
    taskExecutor.setMaxPoolSize(batchConfig.getTaskExecutor().getMaxPoolSize())
    taskExecutor.afterPropertiesSet()
    return taskExecutor
  }

}
