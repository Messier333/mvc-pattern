package com.nhnacademy.springbootmvc;


import com.nhnacademy.springbootmvc.aop.UserAttributeAdvice;
import com.nhnacademy.springbootmvc.controller.InquiryController;
import com.nhnacademy.springbootmvc.domain.Question;
import com.nhnacademy.springbootmvc.domain.User;
import com.nhnacademy.springbootmvc.exception.ValidationFailedException;
import com.nhnacademy.springbootmvc.service.QnaService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InquiryController.class)
class InquiryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QnaService qnaService;

    @MockBean
    UserAttributeAdvice userAttributeAdvice;

    @Test
    void testGetInquiry() throws Exception {
        mockMvc.perform(get("/cs/inquiry").cookie(new Cookie("SESSION", "test1")))
                .andExpect(status().isOk())
                .andExpect(view().name("inquiry"));
    }

    @Test
    void testPostInquirySuccess() throws Exception {
        willDoNothing().given(qnaService).saveQuestion(any(Question.class));
        BDDMockito.given(qnaService.questionNum()).willReturn(10);
        MockMultipartFile imageFile = new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "dummyImageBytes".getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile spyImageFile = spy(imageFile);
        doNothing().when(spyImageFile).transferTo((File) any());

        mockMvc.perform(multipart("/cs/inquiry")
                        .file(spyImageFile)
                        .param("title", "테스트 제목")
                        .param("content", "테스트 내용")
                        .param("category", "OTHER")
                        .flashAttr("user", new User())
                        .flashAttr("question", new Question())
                        .cookie(new Cookie("SESSION", "test1")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs"));
    }

    @Test
    void testPostInquiryValidationFail() throws Exception {
        willThrow(ValidationFailedException.class).given(qnaService).saveQuestion(any());
        MockMultipartFile imageFile = new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "dummyImageBytes".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/cs/inquiry")
                        .file(imageFile)
                        .param("title", "")
                        .param("content", "")
                        .param("category", "OTHER")
                        .flashAttr("user", new User())
                        .flashAttr("question", new Question()).cookie(new Cookie("SESSION", "test1")))
                .andExpect(status().is4xxClientError());
    }

}
