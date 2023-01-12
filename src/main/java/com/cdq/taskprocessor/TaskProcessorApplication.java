package com.cdq.taskprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TaskProcessorApplication {

  public static void main(String[] args) {
    SpringApplication.run(TaskProcessorApplication.class, args);
  }

}
