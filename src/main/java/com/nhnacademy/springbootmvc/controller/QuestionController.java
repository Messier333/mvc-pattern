package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Question;
import com.nhnacademy.springbootmvc.domain.User;
import com.nhnacademy.springbootmvc.service.QnaService;
import jdk.jfr.Category;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cs")
public class QuestionController {
    QnaService qnaService;
    QuestionController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @ModelAttribute("userName")
    public String getUser(@CookieValue(value = "SESSION") String id) {
        return id;
    }

    @GetMapping("/inquiry")
    public String inquiry() {
        return "inquiry";
    }

    @PostMapping("/inquiry")
    public String inquiry(@ModelAttribute("question") Question question, @ModelAttribute("userName") String userName) {
        question.setWriterId(userName);
        qnaService.saveQuestion(question);
        return "redirect:/cs";
    }
}
