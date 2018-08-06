package sgrc.orca.terraform.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("batch")
class BatchConfigurationProperties {

	private RepositoryConfigurationProperties repository = new RepositoryConfigurationProperties()

	private TaskExecutorProperties taskExecutor = new TaskExecutorProperties()

	RepositoryConfigurationProperties getRepository() {
		return repository
	}

	TaskExecutorProperties getTaskExecutor() {
		return taskExecutor
	}

	static class TaskExecutorProperties {

		/**
		 * Core pool size of the thread pool.
		 */
		int corePoolSize = 5

		/**
		 * Queue capacity of the task executor.
		 */
		int queueCapacity = Integer.MAX_VALUE

		/**
		 * Max pool size of the thread pool.
		 */
		int maxPoolSize = Integer.MAX_VALUE

	}

	static class RepositoryConfigurationProperties {

		/**
		 * Database isolation level for creating job executions.
		 */
		String isolationLevelForCreate = null

		/**
		 * Prefix for Spring Batch meta data tables.
		 */
		String tablePrefix = null

	}

}