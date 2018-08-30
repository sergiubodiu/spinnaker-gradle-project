package vportfolio.batch.output.processor.vo;

public class PricingOutputVO {
  private Long id;
  private String started;
  private Boolean completed;

  public PricingOutputVO() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStarted() {
    return started;
  }

  public void setStarted(String started) {
    this.started = started;
  }

  public Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }
}
