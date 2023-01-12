package com.cdq.taskprocessor.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Pattern cannot be longer than input")
public class PatternLongerThanInputException extends RuntimeException {
}
