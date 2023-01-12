package com.cdq.taskprocessor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

import com.cdq.taskprocessor.exceptions.PatternLongerThanInputException;
import com.cdq.taskprocessor.exceptions.TaskNotFoundException;
import com.cdq.taskprocessor.model.Task;
import com.cdq.taskprocessor.model.TaskResult;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TaskServiceTest {

  private final TaskService service = new TaskService();

  private static final long TASK_ID = 1234L;

  @ParameterizedTest
  @MethodSource("taskToExpectedResult")
  void happyPath(Task task, TaskResult expectedResult) throws InterruptedException {
    service.findMatch(task);
    TaskResult actualResult = task.getResult();
    assertEquals(expectedResult, actualResult);
  }

  public static Stream<Arguments> taskToExpectedResult() {
    return Stream.of(
        arguments(new Task("BCD", "ABCD"), new TaskResult(1, 0)),//‘BCD’ matches exactly substring on position 1
        arguments(new Task("BWD", "ABCD"), new TaskResult(1, 1)),//‘BCD’ matches, ‘W’ is a typo here
        arguments(new Task("CFG", "ABCDEFG"), new TaskResult(4, 1)),//EFG’ is better match than ‘CDE’
        arguments(new Task("ABC", "ABCABC"), new TaskResult(0, 0)),//matches exactly twice, selects first
        arguments(new Task("TDD", "ABCDEFG"), new TaskResult(1, 2))//match first - BCD, not CDE
    );
  }

  @Test
  void createNewTaskShouldThrowExceptionWhenPatternLongerThanInput() {
    assertThrows(PatternLongerThanInputException. class, () -> service.createNewTask("qwe", "qw"));
  }

  @Test
  void createNewTaskShouldCreateNewTaskWhenParamsAreOfEqualLength() {
    String pattern = "pattern";
    String input = "input12";
    Task task = service.createNewTask(pattern, input);
    assertEquals(pattern, task.getPattern());
    assertEquals(input, task.getInput());
  }

  @Test
  void createNewTaskShouldReturnTaskWithIdFieldSet() {
    Task task = service.createNewTask("qwerty", "qwerty");
    assertNotNull(task.getId());
  }

  @Test
  void getTaskByIdShouldThrowExceptionWhenTaskNotFound() {
    assertThrows(TaskNotFoundException.class, () -> service.getTaskById(TASK_ID));
  }

  @Test
  void getTaskByIdShouldReturnTask() {
    Task expectedTask = service.createNewTask("abc", "qwerty");
    Task actualTask = service.getTaskById(expectedTask.getId());
    assertEquals(expectedTask, actualTask);
  }
}