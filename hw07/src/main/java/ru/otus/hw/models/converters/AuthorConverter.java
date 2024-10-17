package ru.otus.hw.models.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.Author;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}
