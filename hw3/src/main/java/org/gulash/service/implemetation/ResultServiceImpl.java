package org.gulash.service.implemetation;

import lombok.RequiredArgsConstructor;
import org.gulash.config.TestFileProperties;
import org.gulash.service.LocalizedIOService;
import org.gulash.service.ResultService;
import org.springframework.stereotype.Service;
import org.gulash.domain.TestResult;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestFileProperties testFileProperties;

    private final LocalizedIOService ioService;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printLine("");
        ioService.printLineLocalized("ResultService.test.results");
        ioService.printFormattedLineLocalized(
                "ResultService.student",
                testResult.getStudent().getFullName()
        );
        ioService.printFormattedLineLocalized(
                "ResultService.answered.questions.count",
                testResult.getAnsweredQuestions().size()
        );
        ioService.printFormattedLineLocalized(
                "ResultService.right.answers.count",
                testResult.getRightAnswersCount()
        );

        if (testResult.getRightAnswersCount() >= testFileProperties.rightAnswersCountToPass()) {
            ioService.printLineLocalized("ResultService.passed.test");
            return;
        }
        ioService.printLineLocalized("ResultService.fail.test");
    }
}
