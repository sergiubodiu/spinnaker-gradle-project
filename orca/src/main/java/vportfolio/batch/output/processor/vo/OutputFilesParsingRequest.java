package vportfolio.batch.output.processor.vo;

public class OutputFilesParsingRequest {

  private Long executionId;
  private String workingDirectory;
  private String model;

  public OutputFilesParsingRequest() {
  }

  public OutputFilesParsingRequest(Long executionId, String workingDirectory, String model) {
    this.executionId = executionId;
    this.workingDirectory = workingDirectory;
    this.model = model;
  }

  public Long getExecutionId() {
    return executionId;
  }

  public void setExecutionId(Long executionId) {
    this.executionId = executionId;
  }

  public String getWorkingDirectory() {
    return workingDirectory;
  }

  public void setWorkingDirectory(String workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }
}
