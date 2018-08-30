package vportfolio.batch.web.vo;

public class PricingEngineRequest {

  private String instructionFileName;
  private String workingDirectory;
  private Long executionId;
  private String model;

  public PricingEngineRequest(String instructionFileName, String workingDirectory,
      Long executionId, String model) {
    this.instructionFileName = instructionFileName;
    this.workingDirectory = workingDirectory;
    this.executionId = executionId;
    this.model = model;
  }

  public String getInstructionFileName() {
    return instructionFileName;
  }

  public void setInstructionFileName(String instructionFileName) {
    this.instructionFileName = instructionFileName;
  }

  public String getWorkingDirectory() {
    return workingDirectory;
  }

  public void setWorkingDirectory(String workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  public Long getExecutionId() {
    return executionId;
  }

  public void setExecutionId(Long executionId) {
    this.executionId = executionId;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }
}
