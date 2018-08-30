package vportfolio.batch.output.processor.vo;

public class ContractModelStatusVO {
  private Long contractId;
  private String modeledStatus;
  private Double expectedLoss;
  private String path;

  public ContractModelStatusVO() {
  }

  public Long getContractId() {
    return contractId;
  }

  public void setContractId(Long contractId) {
    this.contractId = contractId;
  }

  public String getModeledStatus() {
    return modeledStatus;
  }

  public void setModeledStatus(String modeledStatus) {
    this.modeledStatus = modeledStatus;
  }

  public Double getExpectedLoss() {
    return expectedLoss;
  }

  public void setExpectedLoss(Double expectedLoss) {
    this.expectedLoss = expectedLoss;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
