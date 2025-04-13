package com.nhnacademy.springbootmvc;

import com.nhnacademy.springbootmvc.domain.Answer;
import com.nhnacademy.springbootmvc.domain.Category;
import com.nhnacademy.springbootmvc.domain.Question;
import com.nhnacademy.springbootmvc.exception.AnswerNotExistException;
import com.nhnacademy.springbootmvc.exception.QuestionNotFoundException;
import com.nhnacademy.springbootmvc.service.QnaServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

@SpringBootTest
class QnaServiceImplTest {

    @MockBean
    QnaServiceImpl qnaService;

    @Test
    void saveQuestion() {
        Question q = new Question();
        q.setTitle("title");
        q.setCategory(Category.OTHER);
        q.setDate(LocalDateTime.now());
        qnaService.saveQuestion(q);
    }

    @Test
    void findQuestion_notExist() {
        try {
            qnaService.findQuestionById(9999L);
        } catch (QuestionNotFoundException e) {
        }
    }

    @Test
    void findAnswer_notExist() {
        Question q = new Question();
        q.setId(9999L);
        try {
            qnaService.findAnswerByQuestion(q);
        } catch (AnswerNotExistException e) {
        }
    }

    @Test
    void saveAnswer() {
        Answer a = new Answer();
        a.setAnswer("answer");
        a.setAnswerDate(LocalDateTime.now());
        qnaService.saveAnswer(a);
    }
}
