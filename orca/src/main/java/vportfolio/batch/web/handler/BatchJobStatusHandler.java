package vportfolio.batch.web.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vportfolio.batch.enums.JobName;
import vportfolio.batch.web.vo.BatchJobStatusResponse;

@Component
public class BatchJobStatusHandler {

  private JobExplorer jobExplorer;
  private static final Logger LOG = LoggerFactory.getLogger(BatchJobStatusHandler.class);

  @Autowired
  public BatchJobStatusHandler(JobExplorer jobExplorer) {
    this.jobExplorer = jobExplorer;
  }

  public void findAllInputJobs(
      List<BatchJobStatusResponse> batchJobStatusResponses, int startIndex, int count) {

    List<JobInstance> inputJobInstances = this.jobExplorer
        .findJobInstancesByJobName(JobName.GENERATE_PRICING_ENGINE_INPUTS.toString(), startIndex,
            count);

    for (JobInstance jobInstance : inputJobInstances) {
      List<JobExecution> jobExecutions = this.jobExplorer.getJobExecutions(jobInstance);
      if (jobExecutions != null && !jobExecutions.isEmpty()) {
        initializeBatchJobStatusResponse(jobExecutions, batchJobStatusResponses);
      }
    }
  }

  public void findAllOutputJobs(
      List<BatchJobStatusResponse> batchJobStatusResponses, int startIndex, int count) {

    List<JobInstance> outputJobInstances = this.jobExplorer
        .findJobInstancesByJobName(JobName.PROCESS_PRICING_ENGINE_OUTPUTS.toString(), startIndex,
            count);

    for (JobInstance jobInstance : outputJobInstances) {
      List<JobExecution> jobExecutions = this.jobExplorer.getJobExecutions(jobInstance);
      hydrateBatchJobStatusResponse(jobExecutions, batchJobStatusResponses);
    }
  }

  public void setAggregatedJobStatus(List<BatchJobStatusResponse> batchJobStatusResponses) {
    for (BatchJobStatusResponse batchJobStatusResponse : batchJobStatusResponses) {
      if ((batchJobStatusResponse.getInputJobStatus() != null && batchJobStatusResponse
          .getInputJobStatus().equals("FAILED")) ||
          (batchJobStatusResponse.getOutputJobStatus() != null && batchJobStatusResponse
              .getOutputJobStatus().equals("FAILED"))) {
        batchJobStatusResponse.setStatus("FAILED");
        batchJobStatusResponse
            .setMessage("One of the jobs have failed, see the job details for more information");
      } else if ((batchJobStatusResponse.getInputJobStatus() != null && batchJobStatusResponse
          .getInputJobStatus().equals("COMPLETED")) &&
          (batchJobStatusResponse.getOutputJobStatus() != null && batchJobStatusResponse
              .getOutputJobStatus().equals("COMPLETED"))) {
        batchJobStatusResponse.setStatus("COMPLETED");
        batchJobStatusResponse.setMessage("Batch successfully completed.");
      } else {
        batchJobStatusResponse.setStatus("IN PROGRESS");
        batchJobStatusResponse.setMessage("Batch is currently in progress...");

      }
    }
  }

  private JobExecution findJobExecutionForId(List<JobExecution> jobExecutions, Long executionId) {
    JobExecution foundJobExecution = null;
    for (JobExecution jobExecution : jobExecutions) {
      Map<String, JobParameter> parameters = jobExecution.getJobParameters().getParameters();
      Long thisExecutionId = Long.valueOf(parameters.get("executionId").getValue().toString());
      if (thisExecutionId.equals(executionId)) {
        foundJobExecution = jobExecution;
        break;
      }
    }
    return foundJobExecution;
  }

  public void findAllRunningInputJobs(List<BatchJobStatusResponse> batchJobStatusResponses) {
    LOG.debug("this.jobExplorer.getJobNames() = " + this.jobExplorer.getJobNames());

    Set<JobExecution> runningJobExecutions = this.jobExplorer
        .findRunningJobExecutions(JobName.GENERATE_PRICING_ENGINE_INPUTS.toString());
    initializeBatchJobStatusResponse(new ArrayList<>(runningJobExecutions),
        batchJobStatusResponses);
  }

  public void findAllRunningOutputJobs(List<BatchJobStatusResponse> batchJobStatusResponses) {
    Set<JobExecution> runningJobExecutions = this.jobExplorer
        .findRunningJobExecutions(JobName.PROCESS_PRICING_ENGINE_OUTPUTS.toString());
    hydrateBatchJobStatusResponse(new ArrayList<>(runningJobExecutions),
        batchJobStatusResponses);
  }

  private void initializeBatchJobStatusResponse(List<JobExecution> jobExecutions,
      List<BatchJobStatusResponse> batchJobStatusResponses) {
    for (JobExecution jobExecution : jobExecutions) {

      BatchJobStatusResponse batchJobStatusResponse = new BatchJobStatusResponse();
      Map<String, JobParameter> parameters = jobExecution.getJobParameters().getParameters();
      JobParameter executionId = parameters.get("executionId");
      batchJobStatusResponse.setExecutionId(Long.valueOf(executionId.getValue().toString()));
      batchJobStatusResponse.setInputJobExecutionId(jobExecution.getJobId());
      batchJobStatusResponse
          .setInputJobStatus(jobExecution.getStatus().getBatchStatus().toString());
      batchJobStatusResponses.add(batchJobStatusResponse);
    }
  }

  private void hydrateBatchJobStatusResponse(List<JobExecution> jobExecutions,
      List<BatchJobStatusResponse> batchJobStatusResponses) {
    for (BatchJobStatusResponse batchJobStatusResponse : batchJobStatusResponses) {
      Long executionId = batchJobStatusResponse.getExecutionId();
      JobExecution jobExecution = findJobExecutionForId(new ArrayList<>(jobExecutions),
          executionId);
      if (jobExecution != null) {
        batchJobStatusResponse.setOutputJobExecutionId(jobExecution.getJobId());
        batchJobStatusResponse
            .setOutputJobStatus(jobExecution.getStatus().getBatchStatus().toString());
      }
    }
  }
}
