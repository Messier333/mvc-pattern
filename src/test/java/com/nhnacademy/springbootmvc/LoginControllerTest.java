package com.nhnacademy.springbootmvc;

import com.nhnacademy.springbootmvc.controller.LoginController;
import com.nhnacademy.springbootmvc.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void testLoginFormWithoutSession() throws Exception {
        mockMvc.perform(get("/cs/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginForm"));
    }

    @Test
    void testLoginFormWithSession() throws Exception {
        mockMvc.perform(get("/cs/login").cookie(new Cookie("SESSION", "user123")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs"));
    }
    @Test
    void testDoLoginSuccess() throws Exception {
        given(userService.match("user123", "pwd123")).willReturn(true);

        mockMvc.perform(post("/cs/login")
                        .param("id", "user123")
                        .param("pwd", "pwd123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs"));
    }

    @Test
    void testDoLoginFail() throws Exception {
        given(userService.match(anyString(), anyString())).willReturn(false);

        mockMvc.perform(post("/cs/login")
                        .param("id", "wrongId")
                        .param("pwd", "wrongPwd"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/login"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(get("/cs/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/login"));
    }
}
