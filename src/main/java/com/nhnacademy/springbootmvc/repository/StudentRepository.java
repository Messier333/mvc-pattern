package com.nhnacademy.springbootmvc.repository;

import com.nhnacademy.springbootmvc.domain.Student;
import org.springframework.stereotype.Component;

public interface StudentRepository {
    boolean matches(String id, String password);

    boolean exists(String id);
    Student register(String id, String password, String name, String email, int score, String comment);
    Student getStudent(String id);
}
