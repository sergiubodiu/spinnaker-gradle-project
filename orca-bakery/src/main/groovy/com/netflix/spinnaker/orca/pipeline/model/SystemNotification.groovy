package com.netflix.spinnaker.orca.pipeline.model;


import java.util.Objects;

public final class SystemNotification {
   private final long createdAt;

   private final String group;

   private final String message;
   private final boolean closed;

   public final long getCreatedAt() {
      return this.createdAt;
   }


   public final String getGroup() {
      return this.group;
   }


   public final String getMessage() {
      return this.message;
   }

   public final boolean getClosed() {
      return this.closed;
   }

   public SystemNotification(long createdAt,  String group,  String message, boolean closed) {
      super();
      this.createdAt = createdAt;
      this.group = group;
      this.message = message;
      this.closed = closed;
   }

   public final long component1() {
      return this.createdAt;
   }


   public final String component2() {
      return this.group;
   }


   public final String component3() {
      return this.message;
   }

   public final boolean component4() {
      return this.closed;
   }

  @Override
  public String toString() {
    return "SystemNotification{" +
      "createdAt=" + createdAt +
      ", group='" + group + '\'' +
      ", message='" + message + '\'' +
      ", closed=" + closed +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SystemNotification that = (SystemNotification) o;
    return createdAt == that.createdAt &&
      closed == that.closed &&
      Objects.equals(group, that.group) &&
      Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {

    return Objects.hash(createdAt, group, message, closed);
  }
}
