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

    @ShellMethod(value = "OpenConsoleH2", key = "oc")
    public String OpenConsoleH2() {
        try {
            Console.main();
            return "Открывается консоль H2";
        } catch (final Exception ex) {
            return "Ошибка при открытии консоли H2: %s".formatted(ex.getLocalizedMessage());
        }
    }
}