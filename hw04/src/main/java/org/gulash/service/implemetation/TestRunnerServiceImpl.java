package org.gulash.service.implemetation;

import lombok.RequiredArgsConstructor;
import org.gulash.domain.Student;
import org.gulash.service.ResultService;
import org.gulash.service.TestRunnerService;
import org.gulash.service.TestService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl
        implements TestRunnerService {

    private final TestService testService;

    private final ResultService resultService;

    @Override
    public void run(Student student) {
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
