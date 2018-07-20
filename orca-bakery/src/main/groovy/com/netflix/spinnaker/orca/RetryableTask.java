package com.netflix.spinnaker.orca;

import com.netflix.spinnaker.orca.pipeline.model.Stage;

import java.time.Duration;

/**
 * A retryable task defines its backoff period (the period between delays) and its timeout (the total period of the task)
 */
public interface RetryableTask extends Task {
  long getBackoffPeriod();

  long getTimeout();

  default long getDynamicBackoffPeriod(Duration taskDuration) {
    return getBackoffPeriod();
  }

  default long getDynamicBackoffPeriod(Stage stage, Duration taskDuration) {
    return getDynamicBackoffPeriod(taskDuration);
  }
}
