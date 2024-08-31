package org.gulash.dao;

import org.gulash.config.TestFileProperties;
import org.gulash.dao.implemetation.CsvQuestionDao;
import org.gulash.domain.Answer;
import org.gulash.domain.Question;
import org.gulash.mapper.LineMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith({SpringExtension.class})
class CsvQuestionDaoTest {
    @Mock
    private TestFileProperties testFileProperties;

    @Mock
    private LineMapper lineMapper;

    @InjectMocks
    private CsvQuestionDao questionDao;

    @Test
    public void testExampleBean() {
        // arrange
        List<Answer> answers = List.of(
                new Answer("1) Correct answer Q1", true),
                new Answer("2) Incorrect answer1 Q1", false),
                new Answer("3) Incorrect answer2 Q1", false)
        );
        Question question = new Question("Question1?", answers);

        when(testFileProperties.name()).thenReturn("test.csv");
        when(testFileProperties.skipLines()).thenReturn(1);
        when(testFileProperties.question()).thenReturn(new TestFileProperties.Question(";"));
        when(testFileProperties.answer()).thenReturn(new TestFileProperties.Answer("\\|", "%"));

        when(lineMapper.toQuestion(any(), any(), any(), any())).thenCallRealMethod();

        // act
        List<Question> all = questionDao.findAll();

        // assert
        assertEquals(all.size(), 1);
        assertEquals(all.get(0), question);
    }
}