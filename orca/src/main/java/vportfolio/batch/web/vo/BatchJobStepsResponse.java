package vportfolio.batch.web.vo;

import java.util.List;

public class BatchJobStepsResponse {

  private List<BatchJobStep> jobSteps;

  public List<BatchJobStep> getJobSteps() {
    return jobSteps;
  }

  public void setJobSteps(List<BatchJobStep> jobSteps) {
    this.jobSteps = jobSteps;
  }
}
