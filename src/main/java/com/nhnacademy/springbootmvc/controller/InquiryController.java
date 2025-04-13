package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Question;
import com.nhnacademy.springbootmvc.domain.User;
import com.nhnacademy.springbootmvc.exception.ValidationFailedException;
import com.nhnacademy.springbootmvc.service.QnaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
    public String inquiry(@Valid @ModelAttribute("question") Question question,
                          @ModelAttribute("user") User user,
                          BindingResult bindingResult,
                          @RequestParam("images") MultipartFile[] images)
            throws IOException {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        question.setWriterId(user.getId());
        List<String> filePaths = new ArrayList<>();

        String projectRootPath = System.getProperty("user.dir");
        int questionNum = qnaService.questionNum();
        Path imagesDir = Paths.get(projectRootPath, "images/" + questionNum);

        Files.createDirectories(imagesDir);

        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                String originalFilename = image.getOriginalFilename();
                log.info("이미지 파일명: {}", originalFilename);

                assert originalFilename != null;
                Path targetLocation = imagesDir.resolve(originalFilename);

                image.transferTo(targetLocation.toFile());


                filePaths.add("/images/"+questionNum+"/" + originalFilename);
            }
        }

        question.setFilePath(filePaths);

        qnaService.saveQuestion(question);

        return "redirect:/cs";
    }
}
