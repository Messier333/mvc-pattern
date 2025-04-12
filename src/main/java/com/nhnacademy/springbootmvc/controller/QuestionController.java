package com.nhnacademy.springbootmvc.controller;


import com.nhnacademy.springbootmvc.domain.Question;
import com.nhnacademy.springbootmvc.domain.User;
import com.nhnacademy.springbootmvc.exception.QuestionNotFoundException;
import com.nhnacademy.springbootmvc.exception.UnauthorizedAccessException;
import com.nhnacademy.springbootmvc.service.QnaService;
import jdk.jfr.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/cs/question/{id}")
public class QuestionController {
    QnaService qnaService;
    QuestionController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @ModelAttribute("question")
    public Question getQuestion(@PathVariable("id") String id, @CookieValue("SESSION") String uid) {
        Question question = qnaService.findQuestionById(Long.parseLong(id));
        if (question == null) {
            throw new QuestionNotFoundException();
        }
        if(!question.getWriterId().equals(uid)) {
            throw new UnauthorizedAccessException();
        }
        return qnaService.findQuestionById(Long.parseLong(id));
    }

    @ModelAttribute("isAnswerExist")
    public boolean isAnswerExist(@ModelAttribute("question") Question question, Model model) {
        if(qnaService.isAnswerExist(question)) {
            model.addAttribute("answer", qnaService.findAnswerByQuestion(question));
            return true;
        }
        return false;
    }

    @GetMapping
    public String questionPage(){
        return "question";
    }

}
