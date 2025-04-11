package com.nhnacademy.springbootmvc.exception;

import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

// TODO #15: validation 예외
public class ValidationFailedException extends RuntimeException {
    public ValidationFailedException(BindingResult bindingResult) {
        super(bindingResult.getAllErrors()
                      .stream()
                      .map(error -> new StringBuilder().append("ObjectName=").append(error.getObjectName())
                              .append(",Message=").append(error.getDefaultMessage())
                              .append(",code=").append(error.getCode()))
                      .collect(Collectors.joining(" | ")));
    }
}