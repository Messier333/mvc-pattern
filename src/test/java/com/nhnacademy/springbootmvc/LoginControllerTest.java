package com.nhnacademy.springbootmvc;

import com.nhnacademy.springbootmvc.controller.LoginController;
import com.nhnacademy.springbootmvc.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LoginController.class)
public class LoginControllerTest {
    @Autowired
    private  MockMvc mockMvc;

    @MockBean
    StudentRepository studentRepository;

    @Test
    void loginTest()throws Exception{
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginForm"));
    }
    @Test
    void doLoginTest()throws Exception{
        when(studentRepository.matches("test1", "test1")).thenReturn(true);
        mockMvc.perform(post("/login").param("id", "test1").param("pwd", "test1"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginSuccess"));
    }
    @Test
    void logoutTest()throws Exception{
        mockMvc.perform(post("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
