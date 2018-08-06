package terraform.config

import terraform.jobs.JobExecutor
import terraform.jobs.JobExecutorLocal
import groovy.util.logging.Slf4j
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Slf4j
@Configuration
class LocalJobConfig {

  @Bean
  @ConditionalOnMissingBean(JobExecutor)
  JobExecutor jobExecutorLocal() {
    JobExecutor jobExecutor = new JobExecutorLocal()

    Runtime.runtime.addShutdownHook(new Thread(new Runnable() {
      @Override
      void run() {
        log.info("Running job executor shutdown hook...")
        int jobCount = jobExecutor.runningJobCount()
        while (jobCount > 0) {
          log.info("Waiting on $jobCount jobs before shutting down...")
          sleep(1000)
          jobCount = jobExecutor.runningJobCount()
        }
      }
    }))

    return jobExecutor
  }
}
