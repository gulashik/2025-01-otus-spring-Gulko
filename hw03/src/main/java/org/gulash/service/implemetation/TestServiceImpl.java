package org.gulash.service.implemetation;

import lombok.RequiredArgsConstructor;
import org.gulash.service.LocalizedIOService;
import org.gulash.service.QuestionService;
import org.gulash.service.TestService;
import org.springframework.stereotype.Service;
import org.gulash.dao.QuestionDao;
import org.gulash.domain.Student;
import org.gulash.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionService questionService;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var isAnswerValid = questionService.askQuestion(question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

}
