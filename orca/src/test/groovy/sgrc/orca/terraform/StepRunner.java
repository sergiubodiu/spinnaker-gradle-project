package sgrc.orca.terraform;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ExecutionContext;

/**
 * Utility class for executing steps outside of a {@link Job}. This is useful in
 * end to end testing in order to allow for the testing of a step individually
 * without running every Step in a job.
 *
 * <ul>
 * <li><b>launchStep(Step step)</b>: Launch the step with new parameters each
 * time. (The current system time will be used)
 * <li><b>launchStep(Step step, JobParameters jobParameters)</b>: Launch the
 * specified step with the provided JobParameters. This may be useful if your
 * step requires a certain parameter during runtime.
 * </ul>
 *
 */
public class StepRunner {

	/** Logger */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private JobLauncher launcher;

	private JobRepository jobRepository;

	public StepRunner(JobLauncher launcher, JobRepository jobRepository) {
		this.launcher = launcher;
		this.jobRepository = jobRepository;
	}

	/**
	 * Launch just the specified step as its own job. A unique set of
	 * JobParameters will automatically be generated. An IllegalStateException
	 * is thrown if there is no Step with the given name.
	 *
	 * @param step The step to launch
	 * @return JobExecution
	 */
	public JobExecution launchStep(Step step) {
		return this.launchStep(step, this.makeUniqueJobParameters(), null);
	}

	/**
	 * Launch just the specified step as its own job. A unique set of
	 * JobParameters will automatically be generated. An IllegalStateException
	 * is thrown if there is no Step with the given name.
	 *
	 * @param step The step to launch
	 * @param jobExecutionContext An ExecutionContext whose values will be
	 * loaded into the Job ExecutionContext prior to launching the step.
	 * @return JobExecution
	 */
	public JobExecution launchStep(Step step, ExecutionContext jobExecutionContext) {
		return this.launchStep(step, this.makeUniqueJobParameters(), jobExecutionContext);
	}

	/**
	 * Launch just the specified step as its own job. An IllegalStateException
	 * is thrown if there is no Step with the given name.
	 *
	 * @param step The step to launch
	 * @param jobParameters The JobParameters to use during the launch
	 * @return JobExecution
	 */
	public JobExecution launchStep(Step step, JobParameters jobParameters) {
		return this.launchStep(step, jobParameters, null);
	}

	/**
	 * Launch just the specified step as its own job. An IllegalStateException
	 * is thrown if there is no Step with the given name.
	 *
	 * @param step The step to launch
	 * @param jobParameters The JobParameters to use during the launch
	 * @param jobExecutionContext An ExecutionContext whose values will be
	 * loaded into the Job ExecutionContext prior to launching the step.
	 * @return JobExecution
	 */
	public JobExecution launchStep(Step step, JobParameters jobParameters, final ExecutionContext jobExecutionContext) {
		//
		// Create a fake job
		//
		SimpleJob job = new SimpleJob();
		job.setName("TestJob");
		job.setJobRepository(this.jobRepository);

		List<Step> stepsToExecute = new ArrayList<Step>();
		stepsToExecute.add(step);
		job.setSteps(stepsToExecute);

		//
		// Dump the given Job ExecutionContext using a listener
		//
		if (jobExecutionContext != null && !jobExecutionContext.isEmpty()) {
			job.setJobExecutionListeners(new JobExecutionListener[] { new JobExecutionListenerSupport() {
				@Override
				public void beforeJob(JobExecution jobExecution) {
					ExecutionContext jobContext = jobExecution.getExecutionContext();
					for (Map.Entry<String, Object> entry : jobExecutionContext.entrySet()) {
						jobContext.put(entry.getKey(), entry.getValue());
					}
				}
			} });
		}

		//
		// Launch the job
		//
		return this.launchJob(job, jobParameters);
	}

	/**
	 * Launch the given job
	 *
	 * @param job
	 * @param jobParameters
	 */
	private JobExecution launchJob(Job job, JobParameters jobParameters) {
		try {
			return this.launcher.run(job, jobParameters);
		}
		catch (JobExecutionAlreadyRunningException e) {
			throw new UnexpectedJobExecutionException("Step runner encountered exception.", e);
		}
		catch (JobRestartException e) {
			throw new UnexpectedJobExecutionException("Step runner encountered exception.", e);
		}
		catch (JobInstanceAlreadyCompleteException e) {
			throw new UnexpectedJobExecutionException("Step runner encountered exception.", e);
		}
		catch (JobParametersInvalidException e) {
			throw new UnexpectedJobExecutionException("Step runner encountered exception.", e);
		}
	}

	/**
	 * @return a new JobParameters object containing only a parameter for the
	 * current timestamp, to ensure that the job instance will be unique
	 */
	private JobParameters makeUniqueJobParameters() {
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
		parameters.put("timestamp", new JobParameter(new Date().getTime()));
		return new JobParameters(parameters);
	}
}