package com.nhnacademy.springbootmvc;

import com.nhnacademy.springbootmvc.controller.AnswerController;
import com.nhnacademy.springbootmvc.domain.Answer;
import com.nhnacademy.springbootmvc.domain.Question;
import com.nhnacademy.springbootmvc.service.QnaService;
import com.nhnacademy.springbootmvc.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AnswerController.class)
class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QnaService qnaService;

    @MockBean
    UserService userService;

    @Test
    void testGetAnswerPage() throws Exception {
        Question mockQuestion = new Question();
        mockQuestion.setId(1L);

        when(qnaService.findQuestionById(1L)).thenReturn(mockQuestion);

        mockMvc.perform(get("/cs/admin/answer/1")
                        .cookie(new Cookie("SESSION", "adminUser")))
                .andExpect(status().isOk())
                .andExpect(view().name("answer"));
    }

    @Test
    void testAnswerSubmit() throws Exception {
        Answer answer = new Answer();
        answer.setId(1L);
        answer.setAnswerWriter("adminUser");
        answer.setAnswerDate(LocalDateTime.now());

        doNothing().when(qnaService).saveAnswer(any(Answer.class));

        mockMvc.perform(post("/cs/admin/answer/1")
                        .cookie(new Cookie("SESSION", "adminUser"))
                        .param("answerTitle", "답변 제목")
                        .param("answerContents", "답변 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"));

        Mockito.verify(qnaService, Mockito.times(1)).saveAnswer(any(Answer.class));
    }
}
