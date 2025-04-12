package com.nhnacademy.springbootmvc.service;

import com.nhnacademy.springbootmvc.domain.Answer;
import com.nhnacademy.springbootmvc.domain.Category;
import com.nhnacademy.springbootmvc.domain.Question;

import java.util.List;
import java.util.Map;

public interface QnaService {
    List<Question> findQuestionByWriter(String writer);
    Answer findAnswerByQuestion(Question question);
    List<String> filePathList(Question question);
    void saveQuestion(Question question);
    List<Question> findQuestionByCategory(Category category);
    boolean isAnswerExist(Question question);
    void saveAnswer(Answer answer);
    public Map<Long, Answer> getAnswerMap();
    public List<Question> unAnsweredQuestion();
}
