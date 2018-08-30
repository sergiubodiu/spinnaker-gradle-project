package vportfolio.batch.app;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;


@EnableBatchProcessing
@SpringBootApplication(scanBasePackages = "vportfolio.batch.app")
public class BatchApplicationTest {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private JobExplorer jobExplorer;

  public static void main(String[] args) {
    SpringApplication.run(BatchApplicationTest.class, args);
  }

  @Bean
  public Job job() {
    return this.jobBuilderFactory.get("job")
        .start(step1())
        .build();
  }

  @Bean
  public Step step1() {
    return this.stepBuilderFactory.get("step1")
        .tasklet((contribution, chunkContext) -> {
          System.out.println(">> number of jobs running: " + jobExplorer.findRunningJobExecutions("job").size());
          return RepeatStatus.FINISHED;
        }).build();
  }


  @Bean
  @Order(Integer.MIN_VALUE)
  public CommandLineRunner beforeCheck() {
    return strings -> {
      System.out.println(">> before Job is run: " + jobExplorer.findRunningJobExecutions("job").size());
    };
  }

  @Bean
  @Order(0)
  public CommandLineRunner jobLauncherCommandLineRunner(JobLauncher jobLauncher) {
    return new JobLauncherCommandLineRunner(jobLauncher, jobExplorer);
  }

  @Bean
  @Order(Integer.MAX_VALUE)
  public CommandLineRunner afterCheck() {
    return strings -> {
      System.out.println(">> after Job is run: " + jobExplorer.findRunningJobExecutions("job").size());
    };
  }
}
