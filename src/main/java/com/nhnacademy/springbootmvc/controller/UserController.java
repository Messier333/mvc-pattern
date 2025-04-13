package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Auth;
import com.nhnacademy.springbootmvc.domain.Category;
import com.nhnacademy.springbootmvc.domain.Question;
import com.nhnacademy.springbootmvc.domain.User;
import com.nhnacademy.springbootmvc.exception.UnauthorizedAccessException;
import com.nhnacademy.springbootmvc.service.QnaService;
import com.nhnacademy.springbootmvc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/cs")
public class UserController {
    UserService userService;
    QnaService qnaService;
    UserController(UserService userService, QnaService qnaService) {
        this.userService = userService;
        this.qnaService = qnaService;
    }


    @GetMapping
    public String userPage(@ModelAttribute("user") User user, Model model, @RequestParam(required = false) String category) {
        if(user.getAuth().equals(Auth.ADMIN)){
            return "redirect:/cs/admin";
        }
        List<Question> questionList;
        if(category!=null&&!category.isEmpty()){
            model.addAttribute("category", category);
            questionList = qnaService.findQuestionByCategory(Category.valueOf(category));
        } else {
            questionList = qnaService.findQuestionByWriter(user.getId());
        }
        model.addAttribute("questions", questionList);
        model.addAttribute("answerMap", qnaService.getAnswerMap());
        return "user";
    }

    @GetMapping("/admin")
    public String adminPage(@ModelAttribute("user") User user, Model model) {
        if (user.getAuth().equals(Auth.USER)) {
            throw new UnauthorizedAccessException();
        }
        List<Question> list = qnaService.unAnsweredQuestion();
        model.addAttribute("questions", list);
        return "admin";
    }

}
