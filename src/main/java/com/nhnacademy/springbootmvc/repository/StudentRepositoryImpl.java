package com.nhnacademy.springbootmvc.repository;

import com.nhnacademy.springbootmvc.domain.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class StudentRepositoryImpl implements StudentRepository {
    private final Map<String, Student> studentMap = new ConcurrentHashMap<>();

    public StudentRepositoryImpl() {
        register("test", "1234", "테스트", "test@test.com", 45, "good");
    }

    @Override
    public boolean matches(String id, String password) {
        return Optional.ofNullable(studentMap.get(id))
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    public boolean exists(String id) {
        return studentMap.containsKey(id);
    }

    @Override
    public Student register(String id, String password, String name, String email, int score, String comment) {
        Student student = new Student(id, password, name, email, score, comment);
        studentMap.put(id, student);
        return student;
    }

    @Override
    public Student getStudent(String id) {
        return studentMap.get(id);
    }
}
