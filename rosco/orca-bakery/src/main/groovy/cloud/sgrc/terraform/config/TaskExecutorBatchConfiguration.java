package cloud.sgrc.terraform.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * This batch infrastructure configuration is quite similar to the
 * {@link org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer}, it only references a
 * {@link org.springframework.core.task.TaskExecutor} used in the
 * {@link org.springframework.batch.core.launch.support.SimpleJobLauncher} for starting jobs asynchronously.
 *
 * @author Tobias Flohre
 * @author Dennis Schulte
 *
 */
@ConditionalOnMissingBean(BatchConfigurer.class)
@Configuration
public class TaskExecutorBatchConfiguration implements BatchConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskExecutorBatchConfiguration.class);

	@Autowired
	private BatchConfigurationProperties batchConfig;

	// Created by TaskExecutorConfiguration if it is used. If an alternative TaskExecutor is configured,
	// it will be injected here.
	@Autowired
	private TaskExecutor taskExecutor;

	private DataSource dataSource;

	private PlatformTransactionManager transactionManager;

	private JobRepository jobRepository;

	private JobLauncher jobLauncher;

	private JobExplorer jobExplorer;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public JobRepository getJobRepository() {
		return jobRepository;
	}

	@Override
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	@Override
	public JobLauncher getJobLauncher() {
		return jobLauncher;
	}

	@Override
	public JobExplorer getJobExplorer() throws Exception {
		return jobExplorer;
	}

	private JobLauncher createJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.setTaskExecutor(taskExecutor);
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}

	protected JobRepository createJobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSource);
		factory.setTransactionManager(transactionManager);
		String isolationLevelForCreate = batchConfig.getRepository().getIsolationLevelForCreate();
		if (isolationLevelForCreate != null) {
			factory.setIsolationLevelForCreate(isolationLevelForCreate);
		}
		String tablePrefix = batchConfig.getRepository().getTablePrefix();
		if (tablePrefix != null) {
			factory.setTablePrefix(tablePrefix);
		}
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@PostConstruct
	public void initialize() throws Exception {
		if (dataSource == null) {
			LOGGER.warn("No datasource was provided...using a Map based JobRepository");

			if (this.transactionManager == null) {
				this.transactionManager = new ResourcelessTransactionManager();
			}

			MapJobRepositoryFactoryBean jobRepositoryFactory = new MapJobRepositoryFactoryBean(this.transactionManager);
			jobRepositoryFactory.afterPropertiesSet();
			this.jobRepository = jobRepositoryFactory.getObject();

			MapJobExplorerFactoryBean jobExplorerFactory = new MapJobExplorerFactoryBean(jobRepositoryFactory);
			jobExplorerFactory.afterPropertiesSet();
			this.jobExplorer = jobExplorerFactory.getObject();
		} else {
			if (this.transactionManager == null) {
				this.transactionManager = new DataSourceTransactionManager(dataSource);
			}

			this.jobRepository = createJobRepository();

			JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
			jobExplorerFactoryBean.setDataSource(this.dataSource);
			String tablePrefix = batchConfig.getRepository().getTablePrefix();
			if (tablePrefix != null) {
				jobExplorerFactoryBean.setTablePrefix(tablePrefix);
			}
			jobExplorerFactoryBean.afterPropertiesSet();
			this.jobExplorer = jobExplorerFactoryBean.getObject();
		}

		this.jobLauncher = createJobLauncher();
	}

}