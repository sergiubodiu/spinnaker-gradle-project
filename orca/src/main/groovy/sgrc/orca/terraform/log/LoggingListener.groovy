package sgrc.orca.terraform.log

import org.slf4j.MDC
import org.springframework.batch.core.*
import org.springframework.core.Ordered
import org.springframework.stereotype.Component

import static sgrc.orca.terraform.log.LoggingFileName.DEFAULT

/**
 * This listener writes the job log file name to the MDC so that it can be picked up by the logging framework for
 * logging to it. It's a {@link JobExecutionListener} and a {@link StepExecutionListener} because in partitioning we may
 * have a lot of {@link StepExecution}s running in different threads.
 * Note that, of the three local parallelization features in Spring Batch, log
 * file separation only works for partitioning and parallel step, not for multi-threaded step.
 * 
 * @author Sergiu Bodiu
 *
 */
@Component
class LoggingListener implements JobExecutionListener, StepExecutionListener, Ordered {

	public static final String JOBLOG_FILENAME = "jobLogFileName"

  @Override
  void beforeJob(JobExecution jobExecution) {
		insertValuesIntoMDC(jobExecution)
  }

	private void insertValuesIntoMDC(JobExecution jobExecution) {
		MDC.put(JOBLOG_FILENAME, DEFAULT.getBaseName(jobExecution))
  }

	@Override
  void afterJob(JobExecution jobExecution) {
		insertValuesIntoMDC(jobExecution)
  }

	private void removeValuesFromMDC() {
		MDC.remove(JOBLOG_FILENAME)
  }

	@Override
  void beforeStep(StepExecution stepExecution) {
		insertValuesIntoMDC(stepExecution.getJobExecution())
  }

	@Override
  ExitStatus afterStep(StepExecution stepExecution) {
		removeValuesFromMDC()
    return null
  }

	@Override
  int getOrder() {
		return HIGHEST_PRECEDENCE
  }

}