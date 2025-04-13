package com.nhnacademy.springbootmvc;

import com.nhnacademy.springbootmvc.domain.Answer;
import com.nhnacademy.springbootmvc.domain.Category;
import com.nhnacademy.springbootmvc.domain.Question;
import com.nhnacademy.springbootmvc.exception.AnswerNotExistException;
import com.nhnacademy.springbootmvc.exception.QuestionNotFoundException;
import com.nhnacademy.springbootmvc.service.QnaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QnaServiceImplTest {

    private QnaServiceImpl qnaService;
    private String questionFilePath;
    private String answerFilePath;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        questionFilePath = tempDir.resolve("question.json").toString();
        answerFilePath = tempDir.resolve("answer.json").toString();
        qnaService = new QnaServiceImpl(questionFilePath, answerFilePath);
    }

    @Test
    void testSaveQuestionAndFindById() {
        Question question = new Question();
        question.setTitle("Test Question");
        question.setWriterId("user1");
        question.setCategory(Category.OTHER);
        qnaService.saveQuestion(question);

        Question result = qnaService.findQuestionById(1L);
        assertNotNull(result);
        assertEquals("Test Question", result.getTitle());
        assertEquals("user1", result.getWriterId());
        assertEquals(Category.OTHER, result.getCategory());
        assertNotNull(result.getDate());
    }

    @Test
    void testFindQuestionByIdNotFound() {
        assertThrows(QuestionNotFoundException.class, () -> {
            qnaService.findQuestionById(999L);
        });
    }

    @Test
    void testUnAnsweredQuestion() {
        Question question1 = new Question();
        question1.setTitle("Q1");
        question1.setWriterId("user1");
        question1.setCategory(Category.OTHER);
        qnaService.saveQuestion(question1);

        Question question2 = new Question();
        question2.setTitle("Q2");
        question2.setWriterId("user2");
        question2.setCategory(Category.COMPLIMENT);
        qnaService.saveQuestion(question2);

        var unanswered = qnaService.unAnsweredQuestion();
        assertEquals(2, unanswered.size());
    }

    @Test
    void testFindQuestionByWriter() {
        Question question1 = new Question();
        question1.setTitle("Q1");
        question1.setWriterId("userA");
        question1.setCategory(Category.OTHER);
        qnaService.saveQuestion(question1);

        Question question2 = new Question();
        question2.setTitle("Q2");
        question2.setWriterId("userB");
        question2.setCategory(Category.COMPLIMENT);
        qnaService.saveQuestion(question2);

        Question question3 = new Question();
        question3.setTitle("Q3");
        question3.setWriterId("userA");
        question3.setCategory(Category.OTHER);
        qnaService.saveQuestion(question3);

        var writerQuestions = qnaService.findQuestionByWriter("userA");
        assertEquals(2, writerQuestions.size());
        writerQuestions.forEach(q -> assertEquals("userA", q.getWriterId()));
    }

    @Test
    void testFindQuestionByCategory() {
        Question question1 = new Question();
        question1.setTitle("Q1");
        question1.setWriterId("userA");
        question1.setCategory(Category.OTHER);
        qnaService.saveQuestion(question1);

        Question question2 = new Question();
        question2.setTitle("Q2");
        question2.setWriterId("userB");
        question2.setCategory(Category.COMPLIMENT);
        qnaService.saveQuestion(question2);

        var others = qnaService.findQuestionByCategory(Category.OTHER);
        assertEquals(1, others.size());
        assertEquals("Q1", others.get(0).getTitle());
    }

    @Test
    void testSaveAnswerAndFindAnswerByQuestion() {
        Question question = new Question();
        question.setTitle("answer");
        question.setWriterId("user");
        question.setCategory(Category.OTHER);
        qnaService.saveQuestion(question);

        assertThrows(AnswerNotExistException.class, () -> qnaService.findAnswerByQuestion(question));

        Answer answer = new Answer();
        answer.setId(1L);
        answer.setAnswer("answer");
        answer.setAnswerDate(LocalDateTime.now());
        qnaService.saveAnswer(answer);

        Answer result = qnaService.findAnswerByQuestion(question);
        assertNotNull(result);
        assertEquals("answer", result.getAnswer());
    }

    @Test
    void testQuestionNum() {
        int initialNum = qnaService.questionNum();
        assertEquals(1, initialNum);

        Question question = new Question();
        question.setTitle("Question");
        question.setWriterId("user1");
        question.setCategory(Category.COMPLIMENT);
        qnaService.saveQuestion(question);

        assertEquals(2, qnaService.questionNum());
    }

    @Test
    void testIsAnswerExist() {
        Question question = new Question();
        question.setTitle("Q1");
        question.setWriterId("user1");
        question.setCategory(Category.OTHER);
        qnaService.saveQuestion(question);

        assertFalse(qnaService.isAnswerExist(question));

        Answer answer = new Answer();
        answer.setId(1L);
        answer.setAnswer("Answer1");
        answer.setAnswerDate(LocalDateTime.now());
        qnaService.saveAnswer(answer);

        assertTrue(qnaService.isAnswerExist(question));
    }

    @Test
    void testGetAnswerMap() {
        Map<Long, Answer> answerMap = qnaService.getAnswerMap();
        assertTrue(answerMap.isEmpty());

        Answer answer = new Answer();
        answer.setId(1L);
        answer.setAnswer("Test Answer");
        answer.setAnswerDate(LocalDateTime.now());
        qnaService.saveAnswer(answer);

        answerMap = qnaService.getAnswerMap();
        assertFalse(answerMap.isEmpty());
        assertEquals("Test Answer", answerMap.get(1L).getAnswer());
    }
}
