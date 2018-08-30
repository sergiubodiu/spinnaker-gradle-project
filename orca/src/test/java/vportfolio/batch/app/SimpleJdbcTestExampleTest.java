package vportfolio.batch.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Michael Minella
 */
@ExtendWith(SpringExtension.class)
@JdbcTest
@SpringBatchTest
@ContextConfiguration(classes = {SimpleJdbcTestExampleTest.BatchConfiguration.class, BatchAutoConfiguration.class})
@TestPropertySource(properties = "debug=true")
public class SimpleJdbcTestExampleTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void test() {
		this.jobLauncherTestUtils.launchStep("step1");
	}

	@Configuration
	@EnableBatchProcessing
	public static class BatchConfiguration {

		@Autowired
		private JobBuilderFactory jobBuilderFactory;

		@Autowired
		private StepBuilderFactory stepBuilderFactory;

		@Bean
		public Job job() {
			return this.jobBuilderFactory.get("job")
					.start(step1())
					.build();
		}

		@Bean
		public Step step1() {
			return this.stepBuilderFactory.get("step1")
					.tasklet((stepContribution, chunkContext) -> {
						System.out.println("I was executed");
						return RepeatStatus.FINISHED;
					})
					.build();
		}
	}
}