package vportfolio.batch.output.processor.vo;

public class IterationExpectedLossByRiskGroupsVO {
  private Long iterationId;
  private Long sequenceId;
  private Long eventId;
  private Double expectedLoss;
  private Double reinstatementPremium;
  private String riskGroup;
  private Double fullRip;

  public IterationExpectedLossByRiskGroupsVO() {
  }

  public Long getIterationId() {
    return iterationId;
  }

  public void setIterationId(Long iterationId) {
    this.iterationId = iterationId;
  }

  public Long getSequenceId() {
    return sequenceId;
  }

  public void setSequenceId(Long sequenceId) {
    this.sequenceId = sequenceId;
  }

  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  public Double getExpectedLoss() {
    return expectedLoss;
  }

  public void setExpectedLoss(Double expectedLoss) {
    this.expectedLoss = expectedLoss;
  }

  public Double getReinstatementPremium() {
    return reinstatementPremium;
  }

  public void setReinstatementPremium(Double reinstatementPremium) {
    this.reinstatementPremium = reinstatementPremium;
  }

  public String getRiskGroup() {
    return riskGroup;
  }

  public void setRiskGroup(String riskGroup) {
    this.riskGroup = riskGroup;
  }

  public Double getFullRip() {
    return fullRip;
  }

  public void setFullRip(Double fullRip) {
    this.fullRip = fullRip;
  }
}
