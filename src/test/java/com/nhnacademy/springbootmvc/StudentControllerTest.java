package com.nhnacademy.springbootmvc;

import com.nhnacademy.springbootmvc.controller.StudentController;
import com.nhnacademy.springbootmvc.domain.Student;
import com.nhnacademy.springbootmvc.repository.StudentRepository;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private  MockMvc mockMvc;

    @MockBean
    StudentRepository studentRepository;

    @Test
    void getStudentTest() throws Exception {
        when(studentRepository.getStudent("test")).thenReturn(Student.createMaskedUser(new Student("test", "testPwd", "testName", "test@test.com", 45, "good")));
        mockMvc.perform(get("/student/test").cookie(new Cookie("SESSION", "test")))
                .andExpect(status().isOk())
                .andExpect(view().name("studentView"));
    }

    @Test
    void getStudentHiddenTest() throws Exception {
        when(studentRepository.getStudent("test")).thenReturn(Student.createMaskedUser(new Student("test", "testPwd", "testName", "test@test.com", 45, "good")));
        mockMvc.perform(get("/student/test").cookie(new Cookie("SESSION", "test")).param("hideScore", "yes"))
                .andExpect(status().isOk())
                .andExpect(view().name("studentView"));
    }

    @Test
    void studentModifyTest() throws Exception {
        when(studentRepository.getStudent("test")).thenReturn(Student.createMaskedUser(new Student("test", "testPwd", "testName", "test@test.com", 45, "good")));

        mockMvc.perform(get("/student/test/modify").cookie(new Cookie("SESSION", "test")))
                .andExpect(status().isOk())
                .andExpect(view().name("studentModify"));
    }

    @Test
    void notFoundTest() throws Exception {
        when(studentRepository.getStudent("test")).thenReturn(Student.createMaskedUser(new Student("test", "testPwd", "testName", "test@test.com", 45, "good")));
        mockMvc.perform(get("/student/test1").cookie(new Cookie("SESSION", "test")))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }
}
