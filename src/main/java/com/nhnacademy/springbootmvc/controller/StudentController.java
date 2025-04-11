package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Student;
import com.nhnacademy.springbootmvc.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @ModelAttribute("student")
    public Student getStudent(@PathVariable("studentId") String studentId) {
        if(studentRepository.getStudent(studentId) == null) {

        }
        return Student.createMaskedUser(studentRepository.getStudent(studentId));
    }


    @GetMapping("/{studentId}")
    public String viewStudent() {
        return "studentView";
    }

    @GetMapping(value = "/{studentId}", params = "hideScore=yes")
    public String viewStudentHidden(@PathVariable("studentId") String studentId, ModelMap modelMap) {
        Student student = studentRepository.getStudent(studentId);
        Student hiddenStudent = new Student(
                student.getId(),
                student.getPassword(),
                student.getName(),
                student.getEmail(),
                0,
                ""
        );
        modelMap.addAttribute("student", Student.createMaskedUser(hiddenStudent));
        return "studentView";
    }

    @GetMapping("/{studentId}/modify")
    public String studentModifyForm(@PathVariable("studentId") String studentId, Model model) {
        return "studentModify";
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(Model model) {
        model.addAttribute("exception", "404 Not Found");
        return "error";
    }
}
