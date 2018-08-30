package vportfolio.batch.web.vo;

public class BatchJobStatusResponse {

  private Long executionId;
  private String status;
  private String message;
  private Long inputJobExecutionId;
  private String inputJobStatus;
  private Long outputJobExecutionId;
  private String outputJobStatus;

  public Long getExecutionId() {
    return executionId;
  }

  public void setExecutionId(Long executionId) {
    this.executionId = executionId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Long getInputJobExecutionId() {
    return inputJobExecutionId;
  }

  public void setInputJobExecutionId(Long inputJobExecutionId) {
    this.inputJobExecutionId = inputJobExecutionId;
  }

  public String getInputJobStatus() {
    return inputJobStatus;
  }

  public void setInputJobStatus(String inputJobStatus) {
    this.inputJobStatus = inputJobStatus;
  }

  public Long getOutputJobExecutionId() {
    return outputJobExecutionId;
  }

  public void setOutputJobExecutionId(Long outputJobExecutionId) {
    this.outputJobExecutionId = outputJobExecutionId;
  }

  public String getOutputJobStatus() {
    return outputJobStatus;
  }

  public void setOutputJobStatus(String outputJobStatus) {
    this.outputJobStatus = outputJobStatus;
  }
}
