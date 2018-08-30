package sgrc.orca.terraform.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("batch")
class BatchConfigurationProperties {

	private TaskExecutorProperties taskExecutor = new TaskExecutorProperties()

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

}