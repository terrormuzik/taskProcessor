package com.cdq.taskprocessor.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "Result not ready. Please check task status first.")
public class ResultNotReadyException extends RuntimeException{

}
