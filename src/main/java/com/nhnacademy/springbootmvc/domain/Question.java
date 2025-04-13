package com.nhnacademy.springbootmvc.domain;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class Question {
    long id;
    @Size(min=2, max=200)
    String title;
    Category category;
    @Size(max=40000)
    String content;
    LocalDateTime date;
    String writerId;
    List<String> filePath;
}
