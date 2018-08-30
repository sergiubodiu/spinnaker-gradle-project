package sgrc.orca.terraform.controllers

import groovy.util.logging.Slf4j
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobOperator
import org.springframework.batch.core.launch.NoSuchJobExecutionException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import sgrc.orca.terraform.config.JobCompletionNotificationListener

/**
 * Controller for delivering monitoring information, like
 * <ul>
 * <li>which jobs are deployed?</li>
 * <li>which jobs are currently running on this machine?</li>
 * <li>detailed information on any running or finished job.</li>
 * </ul>
 * <p>
 * The base url can be set via property sgrc.terraform.monitoring.base, its default is /terraform/monitoring.
 * 
 * Endpoints available:
 * 
 * <ol>
 * <li>Retrieving the names of deployed jobs<br>
 * {base_url}/jobs / GET<br>
 * On success, it returns a JSON array of String containing the names of the deployed jobs.</li>
 * 
 * <li>Retrieving the ids of JobExecutions running on this server<br>
 * {base_url}/jobs/runningexecutions / GET<br>
 * On success, it returns a JSON array containing the ids of the JobExecutions running on this server.</li>
 * 
 * <li>Retrieving the ids of JobExecutions running on this server for a certain job name<br>
 * {base_url}/jobs/runningexecutions/{jobName} / GET<br>
 * On success, it returns a JSON array containing the ids of the JobExecutions running on this server belonging to the
 * specified job.</li>
 * 
 * <li>Retrieving the JobExecution<br>
 * {base_url}/jobs/executions/{executionId} / GET<br>
 * On success, it returns a JSON representation of the JobExecution specified by the id. This representation contains
 * everything you need to know about that job, from job name and BatchStatus to the number of processed items and time
 * used and so on.<br>
 * If the JobExecution cannot be found, a HTTP response code 404 is returned.</li>
 * </ol>
 * 
 * @author Sergiu Bodiu
 *
 */
@Slf4j
@RestController
@RequestMapping('${sgrc.terraform.monitoring.base:/terraform/monitoring}')
class JobMonitoringController {

	private JobOperator jobOperator

	private JobExplorer jobExplorer

	private JobCompletionNotificationListener listener

	JobMonitoringController(JobOperator jobOperator, JobExplorer jobExplorer,
													JobCompletionNotificationListener listener) {
		this.jobOperator = jobOperator
		this.jobExplorer = jobExplorer
		this.listener = listener
	}

	@GetMapping(value = "/jobs")
	Set<String> findRegisteredJobs() throws IOException {
		return new HashSet<>(jobOperator.getJobNames())
	}

	@GetMapping(value = "/jobs/runningexecutions")
	Set<Long> findAllRunningExecutions() {
		return listener.getAllRunningExecutionIds()
	}

	@GetMapping(value = "/jobs/runningexecutions/{jobName}")
	Set<Long> findRunningExecutionsForJobName(@PathVariable String jobName) {
		return listener.getRunningExecutionIdsForJobName(jobName)
	}

	@GetMapping(value = "/jobs/executions/{executionId}")
	JobExecution findExecution(@PathVariable long executionId) throws NoSuchJobExecutionException {
		JobExecution jobExecution = jobExplorer.getJobExecution(executionId)
		if (jobExecution == null) {
			throw new NoSuchJobExecutionException("JobExecution with id " + executionId + " not found.")
		}
		return jobExecution
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoSuchJobExecutionException.class)
	String handleNotFound(Exception ex) {
		log.warn("JobExecution not found.", ex)
		return ex.getMessage()
	}

}