package sgrc.orca.terraform.config

import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import sgrc.orca.terraform.MdcThreadPoolTaskExecutor

import javax.annotation.PostConstruct
import javax.sql.DataSource

import groovy.util.logging.Slf4j
import org.springframework.batch.core.configuration.annotation.BatchConfigurer
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.SimpleJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager

@Slf4j
@ConditionalOnMissingBean(BatchConfigurer.class)
@Configuration
class TaskExecutorBatchConfiguration implements BatchConfigurer {

	private BatchConfigurationProperties batchConfig

	private TaskExecutor taskExecutor

	private DataSource dataSource

	private PlatformTransactionManager transactionManager

	private JobRepository jobRepository

	private JobLauncher jobLauncher

	private JobExplorer jobExplorer

	@Autowired
  TaskExecutorBatchConfiguration (DataSource dataSource, BatchConfigurationProperties batchConfig, TaskExecutor taskExecutor) {
		this.dataSource = dataSource
    this.batchConfig = batchConfig
    this.taskExecutor = taskExecutor
	}

	@Override
	JobRepository getJobRepository() {
		return jobRepository
	}

	@Override
	PlatformTransactionManager getTransactionManager() {
		return transactionManager
	}

	@Override
	JobLauncher getJobLauncher() {
		return jobLauncher
	}

	@Override
	JobExplorer getJobExplorer() throws Exception {
		return jobExplorer
	}

	private JobLauncher createJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher()
		jobLauncher.setJobRepository(jobRepository)
		jobLauncher.setTaskExecutor(taskExecutor)
		jobLauncher.afterPropertiesSet()
		return jobLauncher
	}

	protected JobRepository createJobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean()
		factory.setDataSource(dataSource)
		factory.setTransactionManager(transactionManager)
		String isolationLevelForCreate = batchConfig.getRepository().getIsolationLevelForCreate()
		if (isolationLevelForCreate != null) {
			factory.setIsolationLevelForCreate(isolationLevelForCreate)
		}
		String tablePrefix = batchConfig.getRepository().getTablePrefix()
		if (tablePrefix != null) {
			factory.setTablePrefix(tablePrefix)
		}
		factory.afterPropertiesSet()
		return factory.getObject()
	}

	@PostConstruct
	void initialize() throws Exception {
		if (dataSource == null) {
			log.warn("No datasource was provided...using a Map based JobRepository")

			if (this.transactionManager == null) {
				this.transactionManager = new ResourcelessTransactionManager()
			}

			MapJobRepositoryFactoryBean jobRepositoryFactory = new MapJobRepositoryFactoryBean(this.transactionManager)
			jobRepositoryFactory.afterPropertiesSet()
			this.jobRepository = jobRepositoryFactory.getObject()

			MapJobExplorerFactoryBean jobExplorerFactory = new MapJobExplorerFactoryBean(jobRepositoryFactory)
			jobExplorerFactory.afterPropertiesSet()
			this.jobExplorer = jobExplorerFactory.getObject()
		} else {
			if (this.transactionManager == null) {
				this.transactionManager = new DataSourceTransactionManager(dataSource)
			}

			this.jobRepository = createJobRepository()

			JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean()
			jobExplorerFactoryBean.setDataSource(this.dataSource)
			String tablePrefix = batchConfig.getRepository().getTablePrefix()
			if (tablePrefix != null) {
				jobExplorerFactoryBean.setTablePrefix(tablePrefix)
			}
			jobExplorerFactoryBean.afterPropertiesSet()
			this.jobExplorer = jobExplorerFactoryBean.getObject()
		}

		this.jobLauncher = createJobLauncher()
	}

  @Bean
  TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new MdcThreadPoolTaskExecutor()
    taskExecutor.setCorePoolSize(batchConfig.getTaskExecutor().getCorePoolSize())
    taskExecutor.setQueueCapacity(batchConfig.getTaskExecutor().getQueueCapacity())
    taskExecutor.setMaxPoolSize(batchConfig.getTaskExecutor().getMaxPoolSize())
    taskExecutor.afterPropertiesSet()
    return taskExecutor
  }

}