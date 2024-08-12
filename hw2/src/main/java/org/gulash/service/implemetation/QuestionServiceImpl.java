package org.gulash.service.implemetation;

import lombok.RequiredArgsConstructor;
import org.gulash.domain.Question;
import org.gulash.mapper.QuestionMapper;
import org.gulash.service.IOService;
import org.gulash.service.QuestionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final IOService ioService;

    private final QuestionMapper questionMapper;

    @Override
    public boolean askQuestion(Question question) {

        int answerNumber = ioService.readIntForRangeWithPrompt(
                /*min*/ 1,
                /*max*/ 3,
                /*prompt*/ questionMapper.toFormatedString(question),
                /*errorMessage*/ "Incorrect answer number"
        );

        return checkAnswer(question, answerNumber);
    }

    private boolean checkAnswer(Question question, int answerNumber) {
        int answerIndex = answerNumber - 1;
        return question
                .answers()
                .get(answerIndex)
                .isCorrect();
    }
}
