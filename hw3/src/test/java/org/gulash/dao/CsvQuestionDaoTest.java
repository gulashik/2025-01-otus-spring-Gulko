package org.gulash.dao;



import org.gulash.Application;
import org.gulash.config.TestFileProperties;
import org.gulash.dao.implemetation.CsvQuestionDao;
import org.gulash.domain.Answer;
import org.gulash.domain.Question;
import org.gulash.mapper.LineMapper;
import org.gulash.service.TestRunnerService;
import org.gulash.service.implemetation.TestRunnerServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
//@ActiveProfiles(profiles = {"test"})
// @ExtendWith({SpringExtension.class})
//@ContextConfiguration(classes = {Application.class})
//@TestPropertySource("classpath:application-test.yml")
class CsvQuestionDaoTest {
    @Autowired
    @Spy
    private TestFileProperties fileProperties;

    @InjectMocks
    private CsvQuestionDao questionDao;

    @Mock
    private TestRunnerService testRunnerService;

    @Disabled
    @Test
    public void testExampleBean() {
        // arrange
        List<Answer> answers = List.of(
                new Answer("1) Correct answer Q1", true),
                new Answer("2) Incorrect answer1 Q1", false),
                new Answer("3) Incorrect answer2 Q1", false)
        );
        Question question = new Question("Question1?", answers);

//        Mockito.when(fileProperties.name()).thenReturn("test.csv");

        // act
        List<Question> all = questionDao.findAll();

        // assert
        assertEquals(all.size(), 1);
        assertEquals(all.get(0), question);
    }
}