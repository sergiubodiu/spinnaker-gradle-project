package vportfolio.batch.web.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vportfolio.batch.web.vo.BatchJobStep;
import vportfolio.batch.web.vo.BatchJobStepsRequest;
import vportfolio.batch.web.vo.BatchJobStepsResponse;

@Component
public class BatchJobStepsHandler {

  private JobExplorer jobExplorer;
  private static final Logger LOG = LoggerFactory.getLogger(BatchJobStepsHandler.class);

  @Autowired
  public BatchJobStepsHandler(JobExplorer jobExplorer) {
    this.jobExplorer = jobExplorer;
  }

  public void findBatchJobStepsResponse(
      BatchJobStepsRequest batchJobStepsRequest,
      BatchJobStepsResponse batchJobStepsResponse) {

    if (batchJobStepsRequest.getJobExecutionId() != null) {
      JobExecution jobExecution = this.jobExplorer
          .getJobExecution(batchJobStepsRequest.getJobExecutionId());
      if (jobExecution != null) {
        Collection<StepExecution> inputJobStepExecutions = jobExecution.getStepExecutions();
        batchJobStepsResponse
            .setJobSteps(createBatchJobSteps(inputJobStepExecutions));
      }
    }
  }

  private List<BatchJobStep> createBatchJobSteps(
      Collection<StepExecution> jobStepExecutions) {

    ArrayList<BatchJobStep> batchJobSteps = new ArrayList<>();
    for (StepExecution stepExecution : jobStepExecutions) {
      BatchJobStep batchJobStep = new BatchJobStep();
      batchJobStep.setStepName(stepExecution.getStepName());
      batchJobStep.setStatus(stepExecution.getStatus().getBatchStatus().toString());
      batchJobStep.setMessage(stepExecution.getExitStatus().getExitDescription());
      batchJobSteps.add(batchJobStep);
    }
    return batchJobSteps;
  }
}
