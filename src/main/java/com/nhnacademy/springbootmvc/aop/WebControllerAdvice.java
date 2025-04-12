package com.nhnacademy.springbootmvc.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler
    public String handleException(Exception e, Model model) {
        model.addAttribute("exception", e);
        return "error";
    }
}