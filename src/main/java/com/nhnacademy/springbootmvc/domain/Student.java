package com.nhnacademy.springbootmvc.domain;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Student {

    private String id;
    private String password;
    @NotBlank(message = "이름 공백 없어야함")
    private String name;
    @Email(message = "이메일형식 맞춰야함")
    private String email;
    @Min(value = 0, message = "0보다 커야함")
    @Max(value = 100, message = "100보다 작아야함")
    private int score;
    @NotBlank(message = "코멘트 공백 없어야함")
    @Size(max = 200, message = "최대 200")
    private String comment;

    public static Student create(String id, String password){
        return new Student(id, password);
    }

    private Student(String id, String password){
        this.id = id;
        this.password = password;
    }

    private static final String MASK = "*****";
    public static Student createMaskedUser(Student student){
        Student newStudent = Student.create(student.getId(), MASK);
        newStudent.setComment(student.getComment());
        newStudent.setScore(student.getScore());
        newStudent.setEmail(student.getEmail());
        newStudent.setName(student.getName());
        return newStudent;
    }
}
