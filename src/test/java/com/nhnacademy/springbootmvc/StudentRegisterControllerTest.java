package com.nhnacademy.springbootmvc;

import com.nhnacademy.springbootmvc.controller.StudentRegisterController;
import com.nhnacademy.springbootmvc.domain.Student;
import com.nhnacademy.springbootmvc.repository.StudentRepository;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@Slf4j
@WebMvcTest(StudentRegisterController.class)
public class StudentRegisterControllerTest {
    @Autowired
    private  MockMvc mockMvc;

    @MockBean
    StudentRepository studentRepository;

    @Test
    void studentRegisterFormTest() throws Exception {
        mockMvc.perform(get("/student/register").cookie(new Cookie("SESSION", "test")))
                .andExpect(status().isOk())
                .andExpect(view().name("studentRegister"));
    }

    @Test
    void studentRegisterTest() throws Exception {
        mockMvc.perform(post("/student/register").cookie(new Cookie("SESSION", "test"))
                        .param("id", "test123")
                        .param("password", "testPwd")
                        .param("name", "test123")
                        .param("email", "test@test.com")
                        .param("score", "45")
                        .param("comment", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("studentView"))
                .andDo(print())
                .andExpect(model().attributeExists("student"));
    }
}
