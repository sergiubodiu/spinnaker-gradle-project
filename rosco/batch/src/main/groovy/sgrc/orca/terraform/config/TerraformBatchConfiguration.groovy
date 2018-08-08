package sgrc.orca.terraform.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepExecutionListener
import org.springframework.batch.core.configuration.annotation.*
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.ResourceLoader
import org.springframework.core.task.TaskExecutor
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import sgrc.orca.terraform.JobCompletionNotificationListener
import sgrc.orca.terraform.MdcThreadPoolTaskExecutor
import sgrc.orca.terraform.jobs.Instance
import sgrc.orca.terraform.jobs.InstanceItemProcessor
import sgrc.orca.terraform.log.LoggingListener

import javax.annotation.PostConstruct
import javax.sql.DataSource

@Configuration
@EnableBatchProcessing
class TerraformBatchConfiguration {



  @Autowired
  public JobBuilderFactory jobs

  @Autowired
  public StepBuilderFactory steps

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
  Job importUserJob(JobCompletionNotificationListener listener, LoggingListener loggingListener, Step step1) {
    return jobs.get("importUserJob")
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .listener(loggingListener)
        .flow(step1)
        .end()
        .build()
  }

  @Bean
  Step step1(LoggingListener listener, JdbcBatchItemWriter<Instance> writer) {
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

}