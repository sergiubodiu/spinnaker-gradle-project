package sgrc.orca.terraform;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertEquals;

/**
 * @author Sergiu Bodiu
 */
public class JobLauncherTestUtilsTests {

	@Test
	public void testStepExecutionWithJavaConfig() {
		ApplicationContext context = new AnnotationConfigApplicationContext(TestJobConfiguration.class);

		JobLauncherTestUtils testUtils = context.getBean(JobLauncherTestUtils.class);

		JobExecution execution = testUtils.launchStep("step1");

		Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}

	@Configuration
	@EnableBatchProcessing
	public static class TestJobConfiguration {

		@Autowired
		public JobBuilderFactory jobBuilderFactory;

		@Autowired
		public StepBuilderFactory stepBuilderFactory;

		@Bean
		public Step step() {
			return stepBuilderFactory.get("step1").tasklet(new Tasklet() {
				@Override
				public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					return null;
				}
			}).build();
		}

		@Bean
		public Job job() {
			return jobBuilderFactory.get("job").flow(step()).end().build();
		}

		@Bean
		public JobLauncherTestUtils testUtils() {
			JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
			jobLauncherTestUtils.setJob(job());

			return jobLauncherTestUtils;
		}
	}

}