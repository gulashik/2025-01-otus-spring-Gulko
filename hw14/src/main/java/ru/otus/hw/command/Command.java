package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;


@RequiredArgsConstructor
@ShellComponent
public class Command {
    private final Job migrateJob;

    private final JobLauncher jobLauncher;

    @ShellMethod(value = "startMigration", key = "sm")
    public void startMigration() throws Exception {
        final JobExecution jobExecution = jobLauncher.run(migrateJob, new JobParameters());
        System.out.println(jobExecution);
    }

    @ShellMethod(value = "openConsoleH2", key = "oc")
    public String openConsoleH2() {
        try {
            Console.main();

            return """
                    Opening console H2 see application.yml
                        datasource:
                        url: jdbc:h2:mem:h2db
                        driver-class-name: org.h2.Driver
                        username: root
                        password: root
                    """;
        } catch (final Exception ex) {

            return "Error opening console H2: %s".formatted(ex.getLocalizedMessage());
        }
    }
}