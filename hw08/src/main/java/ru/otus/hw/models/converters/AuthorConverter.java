package ru.otus.hw.models.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.AuthorDto;

@Component
public class AuthorConverter {
    public String authorToString(AuthorDto authorDto) {
        return "Id: %s, FullName: %s".formatted(authorDto.getId(), authorDto.getFullName());
    }
}
