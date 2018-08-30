package vportfolio.batch;

import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vportfolio.batch.enums.JobType;

@Component
public class BatchJobLauncher {

  private JobLauncher jobLauncher;
  private Job processPricingEngineOutputsJob;
  private Job generatePricingEngineInputsJob;

  @Autowired
  public BatchJobLauncher(JobLauncher jobLauncher, Job processPricingEngineOutputsJob,
      Job generatePricingEngineInputsJob) {
    this.jobLauncher = jobLauncher;
    this.processPricingEngineOutputsJob = processPricingEngineOutputsJob;
    this.generatePricingEngineInputsJob = generatePricingEngineInputsJob;
  }

  public JobExecution processPricingEngineOutputsJob(Long executionId, String workingDirectory,
      String model) throws Exception {
    return jobLauncher.run(processPricingEngineOutputsJob,
        createInitialJobParameterMap(executionId, workingDirectory, model, JobType.OUTPUT_PARSING));
  }

  public JobExecution generatePricingEngineInputsJob(Long executionId, String workingDirectory,
      String model)
      throws Exception {
    return jobLauncher.run(generatePricingEngineInputsJob,
        createInitialJobParameterMap(executionId, workingDirectory, model,
            JobType.INPUT_GENERATION));
  }

  private JobParameters createInitialJobParameterMap(Long executionId, String workingDirectory,
      String model, JobType jobType) {
    Map<String, JobParameter> m = new HashMap<>();
    m.put("time", new JobParameter(System.currentTimeMillis()));
    m.put("executionId", new JobParameter(executionId));
    m.put("workingDirectory", new JobParameter(workingDirectory));
    m.put("model", new JobParameter(model));
    m.put("jobType", new JobParameter(jobType.toString()));
    return new JobParameters(m);
  }
}
