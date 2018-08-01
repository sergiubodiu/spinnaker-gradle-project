package cloud.sgrc.terraform.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class BaseConfiguration {

	// Created by spring-boot-starter-batch in combination with our TaskExecutorBatchConfigurer
	@Autowired
	private JobOperator jobOperator;

	@Autowired
	private JobExplorer jobExplorer;

	@Autowired
	private JobRegistry jobRegistry;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobLauncher jobLauncher;

	// Created by spring-boot-starter-jdbc
	@Autowired
	private DataSource dataSource;

	// Created by our TaskExecutorBatchConfigurer if it is used. If an alternative BatchConfigurer is used,
	// a TaskExecutor instance has to be provided somehow.
	@Autowired
	private TaskExecutor taskExecutor;

	public JobOperator jobOperator() {
		return jobOperator;
	}

	public JobExplorer jobExplorer() {
		return jobExplorer;
	}

	public JobRegistry jobRegistry() {
		return jobRegistry;
	}

	public JobRepository jobRepository() {
		return jobRepository;
	}

	public JobLauncher jobLauncher() {
		return jobLauncher;
	}

	public DataSource dataSource() {
		return dataSource;
	}

	public TaskExecutor taskExecutor() {
		return taskExecutor;
	}
}