package cloud.sgrc.terraform.config;

import cloud.sgrc.terraform.controllers.RunningExecutionTracker;
import cloud.sgrc.terraform.controllers.RunningExecutionTrackerListener;
import cloud.sgrc.terraform.controllers.WebConfig;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;

@Configuration
@EnableBatchProcessing(modular = true)
@PropertySource("classpath:batch-web-spring-boot-autoconfigure.properties")
@Import({ WebConfig.class, TaskExecutorBatchConfiguration.class, AutomaticJobRegistrarConfiguration.class,
		BaseConfiguration.class, TaskExecutorConfiguration.class })
@EnableConfigurationProperties({ BatchConfigurationProperties.class })
public class BatchWebAutoConfiguration implements Ordered {

	@Autowired
	private BatchConfigurationProperties batchConfig;

	@Autowired
	private BaseConfiguration baseConfig;

	@Bean
	public RunningExecutionTracker runningExecutionTracker() {
		return new RunningExecutionTracker();
	}

	@Bean
	public RunningExecutionTrackerListener runningExecutionTrackerListener() {
		return new RunningExecutionTrackerListener(runningExecutionTracker());
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}