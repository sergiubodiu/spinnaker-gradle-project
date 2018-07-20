package com.netflix.spinnaker.orca.pipeline.model;

import java.util.*;


public final class DefaultTrigger implements Trigger {

   private Map other;

   private List resolvedExpectedArtifacts;

   private final String type;

   private final String correlationId;

   private final String user;

   private final Map parameters;

   private final List artifacts;

   private final List notifications;
   private boolean isRebake;
   private boolean isDryRun;
   private boolean isStrategy;

  public Map getOther() {
      return this.other;
   }

   public void setOther(Map var1) {
      this.other = var1;
   }


   public List getResolvedExpectedArtifacts() {
      return this.resolvedExpectedArtifacts;
   }

   public void setResolvedExpectedArtifacts(List var1) {
      this.resolvedExpectedArtifacts = var1;
   }


   public String getType() {
      return this.type;
   }


   public String getCorrelationId() {
      return this.correlationId;
   }


   public String getUser() {
      return this.user;
   }


   public Map getParameters() {
      return this.parameters;
   }


   public List getArtifacts() {
      return this.artifacts;
   }


   public List getNotifications() {
      return this.notifications;
   }

   public boolean isRebake() {
      return this.isRebake;
   }

   public void setRebake(boolean var1) {
      this.isRebake = var1;
   }

   public boolean isDryRun() {
      return this.isDryRun;
   }

   public void setDryRun(boolean var1) {
      this.isDryRun = var1;
   }

   public boolean isStrategy() {
      return this.isStrategy;
   }

   public void setStrategy(boolean var1) {
      this.isStrategy = var1;
   }

  public DefaultTrigger(String type) {
    this.type = type;
    other = new LinkedHashMap();
    resolvedExpectedArtifacts = new ArrayList();
    correlationId = "";
    user ="";
    parameters = new LinkedHashMap();
    artifacts = new ArrayList();
    notifications = new ArrayList();
  }

   public DefaultTrigger(String type, String correlationId, String user, Map parameters, List artifacts, List notifications, boolean isRebake, boolean isDryRun, boolean isStrategy) {
      this.type = type;
      this.correlationId = correlationId;
      this.user = user;
      this.parameters = parameters;
      this.artifacts = artifacts;
      this.notifications = notifications;
      this.isRebake = isRebake;
      this.isDryRun = isDryRun;
      this.isStrategy = isStrategy;
      this.other = new LinkedHashMap();
      this.resolvedExpectedArtifacts = new ArrayList();
   }

  @Override
  public String toString() {
    return "DefaultTrigger{" +
      "other=" + other +
      ", resolvedExpectedArtifacts=" + resolvedExpectedArtifacts +
      ", type='" + type + '\'' +
      ", correlationId='" + correlationId + '\'' +
      ", user='" + user + '\'' +
      ", parameters=" + parameters +
      ", artifacts=" + artifacts +
      ", notifications=" + notifications +
      ", isRebake=" + isRebake +
      ", isDryRun=" + isDryRun +
      ", isStrategy=" + isStrategy +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DefaultTrigger that = (DefaultTrigger) o;
    return isRebake == that.isRebake &&
      isDryRun == that.isDryRun &&
      isStrategy == that.isStrategy &&
      Objects.equals(other, that.other) &&
      Objects.equals(resolvedExpectedArtifacts, that.resolvedExpectedArtifacts) &&
      Objects.equals(type, that.type) &&
      Objects.equals(correlationId, that.correlationId) &&
      Objects.equals(user, that.user) &&
      Objects.equals(parameters, that.parameters) &&
      Objects.equals(artifacts, that.artifacts) &&
      Objects.equals(notifications, that.notifications);
  }

  @Override
  public int hashCode() {

    return Objects.hash(other, resolvedExpectedArtifacts, type, correlationId, user, parameters, artifacts, notifications, isRebake, isDryRun, isStrategy);
  }
}
