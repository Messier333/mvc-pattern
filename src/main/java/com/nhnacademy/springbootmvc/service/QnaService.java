package com.nhnacademy.springbootmvc.service;

import com.nhnacademy.springbootmvc.domain.Answer;
import com.nhnacademy.springbootmvc.domain.Category;
import com.nhnacademy.springbootmvc.domain.Question;

import java.util.List;
import java.util.Map;

public interface QnaService {
    List<Question> findQuestionByWriter(String writer);
    Question findQuestionById(Long id);
    Answer findAnswerByQuestion(Question question);
    void saveQuestion(Question question);

    int questionNum();

    List<Question> findQuestionByCategory(Category category);
    boolean isAnswerExist(Question question);
    void saveAnswer(Answer answer);
    Map<Long, Answer> getAnswerMap();
    List<Question> unAnsweredQuestion();
}
