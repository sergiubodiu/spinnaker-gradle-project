package terraform

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

//
//@EnableAutoConfiguration
//@EnableBatchProcessing(modular = true)
//@PropertySource("classpath:batch-web-spring-boot-autoconfigure.properties")
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

	// If you would like to configure your own batch infrastructure via BatchConfigurer,
	// just add a bean of that type to the ApplicationContext, like in the following code.
	// This starter's implementation will step aside then.
	// @Bean
	// public BatchConfigurer batchConfigurer(DataSource dataSource){
	// return new DefaultBatchConfigurer(dataSource);
	// }
}