package org.gulash.dao;

import org.gulash.config.AppProperties;
import org.gulash.domain.Answer;
import org.gulash.domain.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
class CsvQuestionDaoTest {
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @Test
    public void testExampleBean() {
        // arrange
        List<Answer> answers = List.of(
                new Answer("Correct answer Q1", true),
                new Answer("Incorrect answer1 Q1", false),
                new Answer("Incorrect answer2 Q1", false)
        );
        Question question = new Question("Question1?", answers);

        // Не красиво, но по другому не получается
        appProperties.setTestFileName("test.csv");
        // todo Подскажите, пожалуйста, как сделать, чтобы работал вызов? У меня не получилось.
        //Mockito.when(appProperties.getTestFileName()).thenReturn("test.csv");

        // act
        List<Question> all = csvQuestionDao.findAll();

        // assert
        assertEquals(all.size(), 1);
        assertEquals(all.get(0), question);
    }
}