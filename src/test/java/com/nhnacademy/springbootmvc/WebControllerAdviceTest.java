package com.nhnacademy.springbootmvc;

import com.nhnacademy.springbootmvc.controller.WebControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
@WebMvcTest(WebControllerAdvice.class)
public class WebControllerAdviceTest {
    @Autowired
    private  MockMvc mockMvc;

    @Test
    void handleException() throws Exception {
        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("exception"));
    }

}
