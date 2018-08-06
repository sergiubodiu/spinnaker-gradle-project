package sgrc.orca.terraform

import groovy.util.logging.Slf4j
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

import java.util.concurrent.ConcurrentHashMap

/**
 * This listener is needed for tracking the running JobExecutions on this server.
 * 
 * <p>
 * It's easy to find out which jobs are running in general by looking into the database, but it has some drawbacks:
 * <ul>
 * <li>Data in the database might be corrupted. A job may be in status STARTED simply because someone killed the process
 * and Spring Batch didn't have the chance to update the status.</li>
 * <li>We cannot tell from the database on which server the job is running.</li>
 * <li>We might just use an in-memory database, then we cannot access it.</li>
 * </ul>
 *
 * 
 * @author Sergiu Bodiu
 *
 */
@Slf4j
@Component
class JobCompletionNotificationListener implements JobExecutionListener {

  private Map<Long, String> runningExecutions = new ConcurrentHashMap<>()

	@Override
  void beforeJob(JobExecution jobExecution) {
		addRunningExecution(jobExecution.getJobInstance().getJobName(), jobExecution.getId())
  }

	@Override
  void afterJob(JobExecution jobExecution) {
		removeRunningExecution(jobExecution.getId())
  }

  void addRunningExecution(String jobName, Long executionId) {
    runningExecutions.put(executionId, jobName)
  }

  void removeRunningExecution(Long executionId) {
    runningExecutions.remove(executionId)
  }

  Set<Long> getAllRunningExecutionIds() {
    return new HashSet<>(runningExecutions.keySet())
  }

  Set<Long> getRunningExecutionIdsForJobName(String jobName) {
    Set<Long> runningExecutionIds = new HashSet<>()
    for (Map.Entry<Long, String> entry : runningExecutions.entrySet()) {
      if (entry.getValue().equals(jobName)) {
        runningExecutionIds.add(entry.getKey())
      }
    }
    return runningExecutionIds
  }

}