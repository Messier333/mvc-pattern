package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Answer;
import com.nhnacademy.springbootmvc.domain.Question;
import com.nhnacademy.springbootmvc.exception.ValidationFailedException;
import com.nhnacademy.springbootmvc.service.QnaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/cs/admin/answer/{id}")
public class AnswerController {
    QnaService qnaService;
    AnswerController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @ModelAttribute("question")
    public Question getQuestion(@PathVariable("id") String id) {
        return qnaService.findQuestionById(Long.parseLong(id));
    }



    @GetMapping
    public String answer() {
        return "answer";
    }

    @PostMapping
    public String answerSubmit(@PathVariable("id") String id,@Valid @ModelAttribute Answer answer, @CookieValue("SESSION") String uid,
                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        Answer answer1 = answer;
        answer1.setId(Long.parseLong(id));
        answer1.setAnswerWriter(uid);
        answer1.setAnswerDate(LocalDateTime.now());
        qnaService.saveAnswer(answer);
        return "redirect:/cs/admin";
    }
}
