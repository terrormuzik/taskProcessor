package com.cdq.taskprocessor.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskResult {

  int position;
  int typos;
}
