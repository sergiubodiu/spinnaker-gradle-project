package sgrc.orca.terraform.controllers

import groovy.util.logging.Slf4j
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.converter.DefaultJobParametersConverter
import org.springframework.batch.core.converter.JobParametersConverter
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.*
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.JobRestartException
import org.springframework.batch.support.PropertiesConverter
import org.springframework.http.HttpStatus
import org.springframework.util.FileCopyUtils
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import sgrc.orca.terraform.log.LoggingFileName

import javax.batch.operations.JobExecutionAlreadyCompleteException
import javax.batch.operations.JobStartException
import javax.servlet.http.HttpServletResponse

import static sgrc.orca.terraform.log.LoggingFileName.DEFAULT

/**
 * Controller for starting and stopping jobs
 * <ul>
 * <li>which jobs are deployed?</li>
 * <li>which jobs are currently running on this machine?</li>
 * <li>detailed information on any running or finished job.</li>
 * </ul>
 * <p>
 * The base url can be set via property sgrc.terraform.operations.base, its default is /terraform/operations.
 *
 * There are four endpoints available:
 *
 * <ol>
 * <li>Starting jobs<br>
 * {base_url}/jobs/{jobName} / POST<br>
 * Optionally you may define job parameters via request param 'jobParameters'. If a JobParametersIncrementer is
 * specified in the job, it is used to increment the parameters.<br>
 * On success, it returns the JobExecution's id as a plain string.<br>
 * On failure, it returns the message of the Exception as a plain string. There are different failure possibilities:
 * <ul>
 * <li>HTTP response code 404 (NOT_FOUND): the job cannot be found, not deployed on this server.</li>
 * <li>HTTP response code 409 (CONFLICT): the JobExecution already exists and is either running or not restartable.</li>
 * <li>HTTP response code 422 (UNPROCESSABLE_ENTITY): the job parameters didn't pass the validator.</li>
 * <li>HTTP response code 500 (INTERNAL_SERVER_ERROR): any other unexpected failure.</li>
 * </ul>
 * </li>
 *
 * <li>Retrieving an JobExecution's ExitCode<br>
 * {base_url}/jobs/executions/{executionId} / GET<br>
 * On success, it returns the ExitCode of the JobExecution specified by the executionId as a plain string.<br>
 * On failure, it returns the message of the Exception as a plain string.
 * </li>
 *
 * <li>Retrieving a log file for a specific JobExecution<br>
 * {base_url}/jobs/executions/{executionId}/log / GET<br>
 * On success, it returns the log file belonging to the run of the JobExecution specified by the executionId as a plain
 * string.<br>
 * On failure, it returns the message of the Exception as a plain string.
 * </li>
 *
 * <li>Stopping jobs<br>
 * {base_url}/jobs/executions/{executionId} / DELETE<br>
 * On success, it returns true.<br>
 * On failure, it returns the message of the Exception as a plain string. There are different failure possibilities:
 * <ul>
 * <li>HTTP response code 404 (NOT_FOUND): the JobExecution cannot be found.</li>
 * <li>HTTP response code 409 (CONFLICT): the JobExecution is not running.</li>
 * <li>HTTP response code 500 (INTERNAL_SERVER_ERROR): any other unexpected failure.</li>
 * </ul>
 * </li>
 * </ol>
 *
 * @author Sergiu Bodiu
 *
 */
@Slf4j
@RestController
@RequestMapping('${sgrc.terraform.operations.base:/terraform/operations}')
class JobOperationsController {

	public static final String JOB_PARAMETERS = "jobParameters"

	private JobOperator jobOperator

	private JobExplorer jobExplorer

	private JobRegistry jobRegistry

	private JobRepository jobRepository

	private JobLauncher jobLauncher

	private JobParametersConverter jobParametersConverter = new DefaultJobParametersConverter()

	JobOperationsController(JobOperator jobOperator, JobExplorer jobExplorer, JobRegistry jobRegistry,
			JobRepository jobRepository, JobLauncher jobLauncher) {
		this.jobOperator = jobOperator
		this.jobExplorer = jobExplorer
		this.jobRegistry = jobRegistry
		this.jobRepository = jobRepository
		this.jobLauncher = jobLauncher
	}

	@PostMapping(value = "/jobs/{jobName}")
	String launch(@PathVariable String jobName, @RequestParam MultiValueMap<String, String> payload)
			throws NoSuchJobException, JobParametersInvalidException, JobExecutionAlreadyRunningException,
			JobRestartException, JobInstanceAlreadyCompleteException, JobParametersNotFoundException {
		String parameters = payload.getFirst(JOB_PARAMETERS)
		if (log.isDebugEnabled()) {
			log.debug("Attempt to start job with name {} and parameters {}", jobName, parameters)
		}
    Job job = jobRegistry.getJob(jobName)
    JobParameters jobParameters = createJobParametersWithIncrementerIfAvailable(parameters, job)
    Long id = jobLauncher.run(job, jobParameters).getId()
    return String.valueOf(id)
	}

	@GetMapping(value = "/jobs/executions/{executionId}")
	String getStatus(@PathVariable long executionId) throws NoSuchJobExecutionException {
		if (log.isDebugEnabled()) {
			log.debug("Get ExitCode for JobExecution with id: {} ", executionId)
		}
		JobExecution jobExecution = jobExplorer.getJobExecution(executionId)
		if (jobExecution != null) {
			return jobExecution.getExitStatus().getExitCode()
		} else {
			throw new NoSuchJobExecutionException("JobExecution with id " + executionId + " not found.")
		}
	}

	@RequestMapping(value = "/jobs/executions/{executionId}/log", method = RequestMethod.GET)
	void getLogFile(HttpServletResponse response, @PathVariable long executionId)
			throws NoSuchJobExecutionException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("Get log file for job with executionId: {}", executionId)
		}
		String loggingPath = createLoggingPath()
		JobExecution jobExecution = jobExplorer.getJobExecution(executionId)
		if (jobExecution == null) {
			throw new NoSuchJobExecutionException("JobExecution with id " + executionId + " not found.")
		}
		File downloadFile = new File(loggingPath + DEFAULT.getName(jobExecution))
		InputStream is = new FileInputStream(downloadFile)
		FileCopyUtils.copy(is, response.getOutputStream())
		response.flushBuffer()
	}

	@DeleteMapping(value = "/jobs/executions/{executionId}")
	String stop(@PathVariable long executionId)
			throws NoSuchJobExecutionException, JobExecutionNotRunningException {
		if (log.isDebugEnabled()) {
			log.debug("Stop JobExecution with id: {}", executionId)
		}
		Boolean successful = jobOperator.stop(executionId)
		return successful.toString()
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler([NoSuchJobException.class, NoSuchJobExecutionException.class, JobStartException.class ])
	String handleNotFound(Exception ex) {
		log.warn("Job or JobExecution not found.", ex)
		return ex.getMessage()
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(JobParametersNotFoundException.class)
	String handleNoBootstrapParametersCreatedByIncrementer(Exception ex) {
		log.warn("JobParametersIncrementer didn't provide bootstrap parameters.", ex)
		return ex.getMessage()
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler([UnexpectedJobExecutionException.class, JobInstanceAlreadyExistsException.class,
			JobInstanceAlreadyCompleteException.class])
	String handleAlreadyExists(Exception ex) {
		log.warn("JobInstance or JobExecution already exists.", ex)
		return ex.getMessage()
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler([JobExecutionAlreadyRunningException.class, JobExecutionAlreadyCompleteException.class,
			JobRestartException.class ])
	String handleAlreadyRunningOrComplete(Exception ex) {
		log.warn("JobExecution already running or complete.", ex)
		return ex.getMessage()
	}

	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(JobParametersInvalidException.class)
	String handleParametersInvalid(Exception ex) {
		log.warn("Job parameters are invalid.", ex)
		return ex.getMessage()
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(FileNotFoundException.class)
	String handleFileNotFound(Exception ex) {
		log.warn("Logfile not found.", ex)
		return ex.getMessage()
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(JobExecutionNotRunningException.class)
	String handleNotRunning(Exception ex) {
		log.warn("JobExecution is not running.", ex)
		return ex.getMessage()
	}

  private JobParameters createJobParametersWithIncrementerIfAvailable(String parameters, Job job)
      throws JobParametersNotFoundException {
    JobParameters jobParameters = jobParametersConverter
        .getJobParameters(PropertiesConverter.stringToProperties(parameters))
		// use JobParametersIncrementer to create JobParameters if incrementer is set and only if the job is no restart
    if (job.getJobParametersIncrementer() != null) {
      JobExecution lastJobExecution = jobRepository.getLastJobExecution(job.getName(), jobParameters)
			boolean restart = false
			// check if job failed before
      if (lastJobExecution != null) {
        BatchStatus status = lastJobExecution.getStatus()
				if (status.isUnsuccessful() && status != BatchStatus.ABANDONED) {
          restart = true
				}
      }
      // if it's not a restart, create new JobParameters with the incrementer
      if (!restart) {
        JobParameters nextParameters = getNextJobParameters(job)
				Map<String, JobParameter> map = new HashMap<String, JobParameter>(nextParameters.getParameters())
				map.putAll(jobParameters.getParameters())
				jobParameters = new JobParameters(map)
			}
    }
    return jobParameters
	}

  private JobParameters getNextJobParameters(Job job) throws JobParametersNotFoundException {
    String jobIdentifier = job.getName()
		JobParameters jobParameters
		List<JobInstance> lastInstances = jobExplorer.getJobInstances(jobIdentifier, 0, 1)

		JobParametersIncrementer incrementer = job.getJobParametersIncrementer()

		if (lastInstances.isEmpty()) {
      jobParameters = incrementer.getNext(new JobParameters())
			if (jobParameters == null) {
        throw new JobParametersNotFoundException(
            "No bootstrap parameters found from incrementer for job=" + jobIdentifier)
			}
    } else {
      List<JobExecution> lastExecutions = jobExplorer.getJobExecutions(lastInstances.get(0))
			jobParameters = incrementer.getNext(lastExecutions.get(0).getJobParameters())
		}
    return jobParameters
	}

  private String createLoggingPath() {
    String loggingPath = System.getProperty("LOG_PATH")
		if (loggingPath == null) {
      loggingPath = System.getProperty("java.io.tmpdir")
		}
    if (loggingPath == null) {
      loggingPath = "/tmp"
		}
    if (!loggingPath.endsWith("/")) {
      loggingPath = loggingPath + "/"
		}
    return loggingPath
	}

}