package ru.otus.hw.services.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.models.Author;

@Component
public class AuthorMapper {
    public ru.otus.hw.models.dto.Author toDomain(Author entity) {
        if (entity == null) {
            return null;
        }
        return new ru.otus.hw.models.dto.Author(entity.getId(), entity.getFullName());
    }

    public Author toEntity(ru.otus.hw.models.dto.Author domain) {
        if (domain == null) {
            return null;
        }
        return new Author(domain.getId(), domain.getFullName());
    }
}