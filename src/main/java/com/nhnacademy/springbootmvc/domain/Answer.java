package com.nhnacademy.springbootmvc.domain;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
public class Answer {
    long id;
    @Size(min = 1, max = 40000)
    String answer;
    Date answerDate;
    String answerWriter;
}
