package org.gulash.service.implemetation;

import lombok.RequiredArgsConstructor;
import org.gulash.service.TestRunnerService;
import org.gulash.service.TestService;

@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    @Override
    public void run() {
        testService.executeTest();
    }
}
