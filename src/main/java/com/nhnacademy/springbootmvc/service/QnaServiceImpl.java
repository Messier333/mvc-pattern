package com.nhnacademy.springbootmvc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.springbootmvc.domain.Answer;
import com.nhnacademy.springbootmvc.domain.Category;
import com.nhnacademy.springbootmvc.domain.Question;
import com.nhnacademy.springbootmvc.exception.AnswerNotExistException;
import com.nhnacademy.springbootmvc.exception.QuestionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class QnaServiceImpl implements QnaService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Question> questions;
    private final Map<Long, Answer> answerMap;

    @Override
    public Map<Long, Answer> getAnswerMap() {
        return answerMap;
    }

    @Override
    public List<Question> unAnsweredQuestion() {
        List<Question> unanswered = new ArrayList<>();
        for (Question question : questions) {
            if (!isAnswerExist(question)) {
                unanswered.add(question);
            }
        }
        return unanswered;
    }

    public QnaServiceImpl() {

        fileCreate("question.json");
        fileCreate("answer.json");

        try(Reader questionReader = new FileReader("question.json");
        Reader answerReader = new FileReader("answer.json")){
            objectMapper.registerModule(new JavaTimeModule());
            answerMap = new ConcurrentHashMap<>();
            questions = new ArrayList<>(Arrays.asList(objectMapper.readValue(questionReader, Question[].class)));
            for(Answer answer: objectMapper.readValue(answerReader, Answer[].class)) {
                answerMap.put(answer.getId(), answer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fileCreate(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("[]");
            } catch (IOException e) {
                throw new RuntimeException("파일생성실패", e);
            }
        }
    }

    @Override
    public List<Question> findQuestionByWriter(String writer) {
        List<Question> questionList = new ArrayList<>();
        for(Question question: questions) {
            if(question.getWriterId().equals(writer)) {
                questionList.add(question);
            }
        }
        return questionList;
    }

    @Override
    public Question findQuestionById(Long id) {
        for(Question question: questions) {
            if(question.getId() == id) {
                return question;
            }
        }
        throw new QuestionNotFoundException();
    }

    @Override
    public Answer findAnswerByQuestion(Question question) {
        if(!isAnswerExist(question)) {
            throw new AnswerNotExistException();
        }
        return answerMap.get(question.getId());
    }

    @Override
    public void saveQuestion(Question question) {
        question.setId(questions.size()+1);
        question.setDate(LocalDateTime.now());
        questions.add(question);
        try (Writer writer = new FileWriter("question.json")) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, questions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int questionNum(){
        return questions.size()+1;
    }

    @Override
    public List<Question> findQuestionByCategory(Category category) {
        List<Question> questionList = new ArrayList<>();
        for(Question question: questions) {
            if(question.getCategory().equals(category)) {
                questionList.add(question);
            }
        }
        return questionList;
    }


    @Override
    public boolean isAnswerExist(Question question) {
        return answerMap.containsKey(question.getId());
    }

    @Override
    public void saveAnswer(Answer answer) {
        answerMap.put(answer.getId(), answer);
        try (Writer writer = new FileWriter("answer.json")) {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(writer, answerMap.values());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
