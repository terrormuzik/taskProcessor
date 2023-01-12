package com.cdq.taskprocessor.service;

import static com.cdq.taskprocessor.utils.StringSlicer.sliceInputIntoPartsOfLength;
import static com.cdq.taskprocessor.utils.UuidSupplier.getUuidExcluding;

import com.cdq.taskprocessor.exceptions.PatternLongerThanInputException;
import com.cdq.taskprocessor.exceptions.TaskNotFoundException;
import com.cdq.taskprocessor.model.Task;
import com.cdq.taskprocessor.model.TaskResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Slf4j
@RequiredArgsConstructor
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TaskService {

  List<Task> tasks = new ArrayList<>();

  @Async
  public void findMatch(Task task) throws InterruptedException {
    log.info("Finding match for task with id {} started. ", task.getId());
    task.calculateComplexity();

    String input = task.getInput();
    String pattern = task.getPattern();

    if (input.contains(pattern)) {
      taskProcessingDone(task, 0, input.indexOf(pattern));
      return;
    }

    task.calculateComplexity();

    int patternLength = pattern.length();

    Map<Integer, String> positionToInputPart = sliceInputIntoPartsOfLength(input, patternLength);
    task.incrementProgress();

    for (int typos = 1; typos <= patternLength; typos++) {
      for (Entry<Integer, String> entry : positionToInputPart.entrySet()) {
        String inputPart = entry.getValue();
        int typosInPartComparison = countTypos(inputPart, pattern);

        if (typosInPartComparison == typos) {
          taskProcessingDone(task, typos, entry.getKey());
          return;
        }
        task.incrementProgress();
      }
      Thread.sleep(1000);
      task.incrementProgress();
    }
  }

  private void taskProcessingDone(Task task, int typos, Integer position) {
    task.setResult(new TaskResult(position, typos));
    task.getStatus().done();

    log.info("Task processing finished. Task: {}. Result: {}", task, task.getResult());
  }

  private int countTypos(String inputPart, String pattern) {
    int result = 0;
    for (int i = 0; i < pattern.length(); i++) {
      if (!inputPart.substring(i, i + 1).equals(pattern.substring(i, i + 1))) {
        result++;
      }
    }

    return result;
  }

  public Task createNewTask(String pattern, String input) {
    if (pattern.length() > input.length()) {
      throw new PatternLongerThanInputException();
    }

    Task task = new Task(pattern, input);
    task.setId(getUniqueId());
    tasks.add(task);

    log.info("New task created with id {}", task.getId());
    return task;
  }

  private long getUniqueId() {
    List<Long> existingIds = tasks.stream()
        .map(Task::getId)
        .toList();

    return getUuidExcluding(existingIds);
  }

  public Task getTaskById(Long taskId) {
    return tasks.stream()
        .filter(task -> task.getId().equals(taskId))
        .findFirst()
        .orElseThrow(TaskNotFoundException::new);
  }

  public List<Task> getAllTasks() {
    return tasks;
  }
}
