package com.cdq.taskprocessor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class TaskStatus {

  AtomicInteger progress;

  @JsonIgnore
  int complexity;

  public double getProgressPercentage() {
    return (double) progress.get() * 100 / complexity;
  }

  public boolean isDone() {
    return progress.get() == complexity;
  }

  public void done() {
    this.progress.set(complexity);
  }


}
