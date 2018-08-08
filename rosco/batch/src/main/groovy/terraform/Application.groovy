package terraform

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

//
//@EnableAutoConfiguration
//@EnableBatchProcessing(modular = true)
//@PropertySource("classpath:application.properties")
//@Import([WebConfig.class,
//	TaskExecutorBatchConfiguration.class,
//	AutomaticJobRegistrarConfiguration.class,
//	BaseConfiguration.class,
//	TaskExecutorConfiguration.class ])
//@EnableConfigurationProperties([ BatchConfigurationProperties.class ])

@SpringBootApplication
public class Application {

//	@Bean
//	public RunningExecutionTracker runningExecutionTracker() {
//		return new RunningExecutionTracker();
//	}
//
//	@Bean
//	public RunningExecutionTrackerListener runningExecutionTrackerListener() {
//		return new RunningExecutionTrackerListener(runningExecutionTracker());
//	}
//
//	@Override
//	public int getOrder() {
//		return Ordered.LOWEST_PRECEDENCE;
//	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}