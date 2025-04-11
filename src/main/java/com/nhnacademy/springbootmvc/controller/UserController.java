package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.User;
import com.nhnacademy.springbootmvc.exception.UserNotFoundException;
import com.nhnacademy.springbootmvc.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cs")
public class UserController {
    UserService userService;
    UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User getUser(@CookieValue(value = "SESSION") String id) {
        if(!userService.exist(id)){
            throw new UserNotFoundException();
        }
        return userService.getUser(id);
    }
    @GetMapping
    public String mainPage() {
        return "user";
    }
}
