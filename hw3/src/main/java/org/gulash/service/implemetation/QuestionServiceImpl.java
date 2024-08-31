package org.gulash.service.implemetation;

import lombok.RequiredArgsConstructor;
import org.gulash.domain.Answer;
import org.gulash.domain.Question;
import org.gulash.service.QuestionService;
import org.gulash.service.gateway.LocalizedMessagesServiceGateway;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final IOServiceImpl ioService;

    private final LocalizedMessagesServiceGateway localizedMessagesService;

    @Override
    public boolean askQuestion(Question question) {

        int answerNumber = ioService.readIntForRangeWithPrompt(
                /*min*/ 1,
                /*max*/ 3,
                /*prompt*/ toFormatedString(question),
                /*errorMessage*/ localizedMessagesService.getMessage("Error.message.incorrect.answer.number")
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

    private String toFormatedString(Question question) {
        Objects.requireNonNull(question);

        StringBuilder resultString = new StringBuilder();
        resultString.append(question.text()).append("\n");

        for (Answer answer : question.answers()) {
            resultString.append(answer.text()).append("\n");
        }

        return resultString.toString();
    }
}
