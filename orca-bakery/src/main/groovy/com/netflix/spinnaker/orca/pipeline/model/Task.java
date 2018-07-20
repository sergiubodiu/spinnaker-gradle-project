/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.orca.pipeline.model;

import com.netflix.spinnaker.orca.ExecutionStatus;

import static com.netflix.spinnaker.orca.ExecutionStatus.NOT_STARTED;

/**
 * A "task" is a component piece of a stage
 */
public class Task {
  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  private String implementingClass;

  public String getImplementingClass() {
    return implementingClass;
  }

  public void setImplementingClass(String implementingClass) {
    this.implementingClass = implementingClass;
  }

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  private Long startTime;

  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  private Long endTime;

  public Long getEndTime() {
    return endTime;
  }

  public void setEndTime(Long endTime) {
    this.endTime = endTime;
  }

  private ExecutionStatus status = NOT_STARTED;

  public ExecutionStatus getStatus() {
    return status;
  }

  public void setStatus(ExecutionStatus status) {
    this.status = status;
  }

  private boolean stageStart;

  public boolean isStageStart() {
    return stageStart;
  }

  public void setStageStart(boolean stageStart) {
    this.stageStart = stageStart;
  }

  private boolean stageEnd;

  public boolean isStageEnd() {
    return stageEnd;
  }

  public void setStageEnd(boolean stageEnd) {
    this.stageEnd = stageEnd;
  }

  private boolean loopStart;

  public boolean isLoopStart() {
    return loopStart;
  }

  public void setLoopStart(boolean loopStart) {
    this.loopStart = loopStart;
  }

  private boolean loopEnd;

  public boolean isLoopEnd() {
    return loopEnd;
  }

  public void setLoopEnd(boolean loopEnd) {
    this.loopEnd = loopEnd;
  }
}
