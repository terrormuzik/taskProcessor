package com.cdq.taskprocessor.controller;

import com.cdq.taskprocessor.exceptions.ResultNotReadyException;
import com.cdq.taskprocessor.model.Task;
import com.cdq.taskprocessor.model.TaskResult;
import com.cdq.taskprocessor.service.TaskService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@Slf4j
@AllArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @PostMapping
  public Task createTask(@RequestParam String pattern, @RequestParam String input)
      throws InterruptedException {
    log.info("createTask method invoked with the following arguments: pattern: {}, input: {}",
        pattern, input);

    Task task = taskService.createNewTask(pattern, input);
    taskService.findMatch(task);

    return task;
  }

  @GetMapping("/status")
  public double getTaskStatus(@RequestParam Long taskId) {
    log.info("getTaskStatus method invoked with the following argument: taskId: {}", taskId);

    Task taskById = taskService.getTaskById(taskId);

    return taskById.getStatus().getProgressPercentage();
  }

  @GetMapping("/result")
  public TaskResult getTaskResult(@RequestParam Long taskId) {
    log.info("getTaskResult method invoked with the following argument: taskId: {}", taskId);

    Task taskById = taskService.getTaskById(taskId);

    if (!taskById.ready()) {
      throw new ResultNotReadyException();
    }

    return taskById.getResult();
  }


  @GetMapping("/all")
  public List<Task> getAllTasks() {
    log.info("getAllTasks method invoked");
    return taskService.getAllTasks();
  }

}
