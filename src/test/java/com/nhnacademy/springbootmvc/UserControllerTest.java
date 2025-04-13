package com.nhnacademy.springbootmvc;

import com.nhnacademy.springbootmvc.domain.Auth;
import com.nhnacademy.springbootmvc.domain.User;
import com.nhnacademy.springbootmvc.exception.UnauthorizedAccessException;
import com.nhnacademy.springbootmvc.service.QnaService;
import com.nhnacademy.springbootmvc.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.Cookie;
import java.util.ArrayList;

import static org.mockito.BDDMockito.when;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    QnaService qnaService;

    @Test
    void userPage_normalUser() throws Exception {
        User normal = new User();
        normal.setId("user1");
        normal.setAuth(Auth.USER);
        when(userService.getUser("user1")).thenReturn(normal);
        when(qnaService.findQuestionByWriter("user1")).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/cs").cookie(new Cookie("SESSION", "user1")))
                .andExpect(status().isOk())
                .andExpect(view().name("user"));
    }

    @Test
    void userPage_admin() throws Exception {
        User admin = new User();
        admin.setId("admin");
        admin.setAuth(Auth.ADMIN);
        when(userService.getUser("admin")).thenReturn(admin);
        mockMvc.perform(get("/cs").cookie(new Cookie("SESSION", "admin")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"));
    }

    @Test
    void userPage_noCookie() throws Exception {
        mockMvc.perform(get("/cs"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void adminPage_success() throws Exception {
        User admin = new User();
        admin.setId("admin");
        admin.setAuth(Auth.ADMIN);
        when(userService.getUser("admin")).thenReturn(admin);
        when(qnaService.unAnsweredQuestion()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/cs/admin").cookie(new Cookie("SESSION", "admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));
    }

    @Test
    void adminPage_unauthorized() throws Exception {
        User normal = new User();
        normal.setId("user1");
        normal.setAuth(Auth.USER);
        when(userService.getUser("user1")).thenReturn(normal);
        willThrow(new UnauthorizedAccessException()).given(qnaService).unAnsweredQuestion();
        mockMvc.perform(get("/cs/admin").cookie(new Cookie("SESSION", "user1")))
                .andExpect(status().is4xxClientError());
    }
}
