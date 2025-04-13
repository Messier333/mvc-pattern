package com.nhnacademy.springbootmvc;


import com.nhnacademy.springbootmvc.domain.Category;
import com.nhnacademy.springbootmvc.domain.Question;
import com.nhnacademy.springbootmvc.domain.User;
import com.nhnacademy.springbootmvc.exception.QuestionNotFoundException;
import com.nhnacademy.springbootmvc.exception.UnauthorizedAccessException;
import com.nhnacademy.springbootmvc.service.QnaService;
import com.nhnacademy.springbootmvc.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.BDDMockito.when;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class QuestionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QnaService qnaService;

    @MockBean
    UserService userService;

    @BeforeEach
    void setUp() {
        Question q = new Question();
        q.setId(1L);
        q.setWriterId("user1");
        when(qnaService.findQuestionById(1L)).thenReturn(q);
        when(qnaService.isAnswerExist(q)).thenReturn(false);
    }

    @Test
    void getQuestion_withCookie_correctUser() throws Exception {
        Question q = new Question();
        q.setId(1L);
        q.setWriterId("user1");
        q.setCategory(Category.OTHER);
        when(qnaService.findQuestionById(1L)).thenReturn(q);
        when(userService.getUser("user1")).thenReturn(new User());
        mockMvc.perform(get("/cs/question/1").cookie(new Cookie("SESSION", "user1")))
                .andExpect(status().isOk())
                .andExpect(view().name("question"));
    }

    @Test
    void getQuestion_noCookie() throws Exception {
        mockMvc.perform(get("/cs/question/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getQuestion_wrongUser() throws Exception {
        willThrow(new UnauthorizedAccessException()).given(qnaService).findQuestionById(2L);
        mockMvc.perform(get("/cs/question/2").cookie(new Cookie("SESSION", "notAuth")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getQuestion_notFound() throws Exception {
        willThrow(new QuestionNotFoundException()).given(qnaService).findQuestionById(99L);
        mockMvc.perform(get("/cs/question/99").cookie(new Cookie("SESSION", "user1")))
                .andExpect(status().is4xxClientError());
    }
}

