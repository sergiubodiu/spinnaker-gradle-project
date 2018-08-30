package sgrc.orca.terraform.log

import groovy.util.logging.Slf4j
import org.springframework.batch.core.*
import org.springframework.core.Ordered

import java.util.Map.Entry

/**
 * This listener adds a protocol header and a protocol summary to the log.
 * 
 * @author Sergiu Bodiu
 * 
 */
@Slf4j
class ProtocolListener implements JobExecutionListener, Ordered {

	private static final int DEFAULT_WIDTH = 80
  private static final String HYPHEN = createFilledLine('-' as char)
  private static final String STAR = createFilledLine('*' as char)

  @Override
  void afterJob(JobExecution jobExecution) {
		StringBuilder protocol = new StringBuilder()
    protocol.append("\n")
    protocol.append(STAR)
    protocol.append(HYPHEN)
    protocol.append("Protocol for " + jobExecution.getJobInstance().getJobName() + " \n")
    protocol.append("  Started:      " + jobExecution.getStartTime() + "\n")
    protocol.append("  Finished:     " + jobExecution.getEndTime() + "\n")
    protocol.append("  Exit-Code:    " + jobExecution.getExitStatus().getExitCode() + "\n")
    protocol.append("  Exit-Descr:   " + jobExecution.getExitStatus().getExitDescription() + "\n")
    protocol.append("  Status:       " + jobExecution.getStatus() + "\n")
    protocol.append("  Content of Job-ExecutionContext:\n")
    for (Entry<String, Object> entry : jobExecution.getExecutionContext().entrySet()) {
			protocol.append("  " + entry.getKey() + "=" + entry.getValue() + "\n")
    }
		protocol.append("  Job-Parameter: \n")
    for (Entry<String, JobParameter> entry : jobExecution.getJobParameters().getParameters().entrySet()) {
      protocol.append("  " + entry.getKey() + "=" + entry.getValue() + "\n")
    }

		protocol.append(HYPHEN)
    for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
			protocol.append("Step " + stepExecution.getStepName() + " \n")
      protocol.append("  ReadCount:    " + stepExecution.getReadCount() + "\n")
      protocol.append("  WriteCount:   " + stepExecution.getWriteCount() + "\n")
      protocol.append("  Commits:      " + stepExecution.getCommitCount() + "\n")
      protocol.append("  SkipCount:    " + stepExecution.getSkipCount() + "\n")
      protocol.append("  Rollbacks:    " + stepExecution.getRollbackCount() + "\n")
      protocol.append("  Filter:       " + stepExecution.getFilterCount() + "\n")
      protocol.append("  Content of Step-ExecutionContext:\n")
      for (Entry<String, Object> entry : stepExecution.getExecutionContext().entrySet()) {
				protocol.append("  " + entry.getKey() + "=" + entry.getValue() + "\n")
      }
			protocol.append(HYPHEN)
    }
		protocol.append(STAR)
    log.info(protocol.toString())
  }

	@Override
  void beforeJob(JobExecution jobExecution) {
		StringBuilder protocol = new StringBuilder()
    protocol.append(HYPHEN)
    protocol.append("Job " + jobExecution.getJobInstance().getJobName() + " started with Job-Execution-Id "
				+ jobExecution.getId() + " \n")
    protocol.append("Job-Parameter: \n")
    for (Entry<String, JobParameter> entry : jobExecution.getJobParameters().getParameters().entrySet()) {
      protocol.append("  " + entry.getKey() + "=" + entry.getValue() + "\n")
    }
		protocol.append(HYPHEN)
    log.info(protocol.toString())
  }

	@Override
  int getOrder() {
		return HIGHEST_PRECEDENCE + 10
  }

	/**
	 * Create line with defined char
	 * 
	 * @param filler
	 */
	private static String createFilledLine(char filler) {
    return padding(DEFAULT_WIDTH, filler) + "\n"
  }

  private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
    final char[] buf = new char[repeat]
    for (int i = 0; i < buf.length; i++) {
      buf[i] = padChar
    }
    return new String(buf)
  }

}