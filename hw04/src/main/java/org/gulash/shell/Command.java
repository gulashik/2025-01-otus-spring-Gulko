package org.gulash.shell;

import lombok.RequiredArgsConstructor;
import org.gulash.service.TestRunnerService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent(value = "Commands for student test")
public class Command {

    private final TestRunnerService testRunnerService;

    @ShellMethod(
            value = "Student command",
            key = {"s", "stud", "student"}
    )
    public void getStudentName(
        String studentFirstName,
        String studentLastName
    ) {
        testRunnerService.run(studentFirstName, studentLastName);
    }
}
