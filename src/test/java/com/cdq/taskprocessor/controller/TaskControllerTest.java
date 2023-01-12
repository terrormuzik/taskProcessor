package com.cdq.taskprocessor.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cdq.taskprocessor.exceptions.ResultNotReadyException;
import com.cdq.taskprocessor.exceptions.TaskNotFoundException;
import com.cdq.taskprocessor.model.Task;
import com.cdq.taskprocessor.model.TaskResult;
import com.cdq.taskprocessor.model.TaskStatus;
import com.cdq.taskprocessor.service.TaskService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TaskControllerTest {

  @Mock
  private TaskService service;

  @InjectMocks
  private TaskController controller;

  private static final Long TASK_ID = 1234L;

  private Task task;

  @BeforeEach
  void setUp() {
    task = new Task("abcd", "abcdefg");
    task.setStatus(new TaskStatus(new AtomicInteger(7), 11));
  }

  @Test
  void createTaskShouldCallService() throws InterruptedException {
    String pattern = "qwe";
    String input = "rty";
    Task task = controller.createTask(pattern, input);
    verify(service, times(1)).createNewTask(pattern, input);
    verify(service, times(1)).findMatch(task);
  }

  @Test
  void getTaskStatusShouldCallService() {
    when(service.getTaskById(TASK_ID)).thenReturn(task);
    controller.getTaskStatus(TASK_ID);
    verify(service, times(1)).getTaskById(TASK_ID);
  }

  @Test
  void getTaskStatusShouldReturnPercentageValue() {
    when(service.getTaskById(TASK_ID)).thenReturn(task);
    double actualProgress = controller.getTaskStatus(TASK_ID);
    assertEquals(task.getStatus().getProgressPercentage(), actualProgress);
  }

  @Test
  void getTaskResultShouldCallService() {
    task.getStatus().done();
    when(service.getTaskById(TASK_ID)).thenReturn(task);
    controller.getTaskResult(TASK_ID);
    verify(service, times(1)).getTaskById(TASK_ID);
  }

  @Test
  void getTaskResultShouldThrowExceptionWhenResultNotReady() {
    when(service.getTaskById(TASK_ID)).thenReturn(task);
    assertThrows(ResultNotReadyException.class, () -> controller.getTaskResult(TASK_ID));
  }

  @Test
  void getTaskResultShouldReturnTaskResultIfTaskIsDone() {
    task.setResult(new TaskResult(0, 0));
    task.getStatus().done();

    when(service.getTaskById(TASK_ID)).thenReturn(task);
    TaskResult actualResult = controller.getTaskResult(TASK_ID);

    assertEquals(task.getResult(), actualResult);
  }

  @Test
  void getAllTasksShouldCallService() {
    controller.getAllTasks();
    verify(service, times(1)).getAllTasks();
  }

  @Test
  void getAllTasksShouldReturnAllTasks() {
    List<Task> expectedResult = new ArrayList<>();
    expectedResult.add(new Task("wer", "qwerty"));
    expectedResult.add(new Task("abc", "abdfgtr"));

    when(service.getAllTasks()).thenReturn(expectedResult);
    List<Task> actualResult = controller.getAllTasks();

    assertEquals(expectedResult, actualResult);
  }
}