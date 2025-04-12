package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Question;
import com.nhnacademy.springbootmvc.domain.User;
import com.nhnacademy.springbootmvc.exception.ValidationFailedException;
import com.nhnacademy.springbootmvc.service.QnaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/cs")
public class InquiryController {
    QnaService qnaService;
    InquiryController(QnaService qnaService){
        this.qnaService = qnaService;
    }

    @GetMapping("/inquiry")
    public String inquiry() {
        return "inquiry";
    }

    @PostMapping("/inquiry")
    public String inquiry(@Valid @ModelAttribute("question") Question question, @ModelAttribute("user") User user,
                          BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        question.setWriterId(user.getId());
        qnaService.saveQuestion(question);
        return "redirect:/cs";
    }

}
