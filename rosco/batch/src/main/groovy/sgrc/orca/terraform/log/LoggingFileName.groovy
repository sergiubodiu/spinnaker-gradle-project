package sgrc.orca.terraform.log

import org.springframework.batch.core.JobExecution

/**
 * @author Sergiu Bodiu
 */
enum LoggingFileName {

	DEFAULT()

	private static final String DEFAULT_EXTENSION = ".log"

	String getName(JobExecution jobExecution) {
		return getBaseName(jobExecution) + DEFAULT_EXTENSION
	}

	String getBaseName(JobExecution jobExecution) {
		return "batch-" + jobExecution.getJobInstance().getJobName() + "-" + Long.toString(jobExecution.getId())
	}

}