package com.cdq.taskprocessor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Task {

  Long id;

  String pattern;
  String input;

  @JsonIgnore
  TaskStatus status;

  @JsonIgnore
  TaskResult result;

  public Task(String pattern, String input) {
    this.pattern = pattern;
    this.input = input;
  }

  //this method counts approx. complexity by finding the pessimistic variant (100% typos, max iterations)
  public void calculateComplexity() {
    int maxIterationsNeeded = pattern.length() + input.length();
    this.setStatus(new TaskStatus(new AtomicInteger(0), maxIterationsNeeded));
  }

  public boolean ready() {
    return status != null && status.isDone();
  }

  public void incrementProgress() {
    status.getProgress().getAndIncrement();
  }
}
