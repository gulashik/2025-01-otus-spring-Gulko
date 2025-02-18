package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@RequiredArgsConstructor
@ShellComponent
public class ShellCommands {
    private final ConfigurableApplicationContext context;

    @ShellMethod(value = "Команда для завершения приложения", key = "exit")
    public void exit(@ShellOption(defaultValue = "0") int code) {
        System.out.println("Exit executed. Exiting with code: " + code);
        context.close();
        System.exit(code);
    }
}
