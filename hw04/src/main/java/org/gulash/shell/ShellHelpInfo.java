package org.gulash.shell;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ShellHelpInfo {
    @PostConstruct
    private void print() {
        var info = """
                ***********************************************************
                
                Ввод имени через команду "student имя фамилия"
                Для выхода нужно "exit"
                Для просмотра доступных команда "help"
                
                ***********************************************************
                """;
        System.out.println(info);
    }
}