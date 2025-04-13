package com.nhnacademy.springbootmvc.aop;

import com.nhnacademy.springbootmvc.controller.AnswerController;
import com.nhnacademy.springbootmvc.controller.InquiryController;
import com.nhnacademy.springbootmvc.controller.QuestionController;
import com.nhnacademy.springbootmvc.controller.UserController;
import com.nhnacademy.springbootmvc.domain.User;
import com.nhnacademy.springbootmvc.exception.UserNotFoundException;
import com.nhnacademy.springbootmvc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice(assignableTypes = {QuestionController.class, InquiryController.class, UserController.class, AnswerController.class})
public class UserAttributeAdvice {

    private final UserService userService;

    public UserAttributeAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User getUser(@CookieValue(value = "SESSION") String id) {
        log.info("getUser:{}", id);
        if (userService.exist(id)) {
            throw new UserNotFoundException();
        }
        return userService.getUser(id);
    }
}
