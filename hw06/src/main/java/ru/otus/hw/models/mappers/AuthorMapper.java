package ru.otus.hw.models.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.domains.Author;
import ru.otus.hw.models.entities.AuthorEntity;

@Component
public class AuthorMapper {
    public Author toDomain(AuthorEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Author(entity.getId(), entity.getFullName());
    }

    public AuthorEntity toEntity(Author domain) {
        if (domain == null) {
            return null;
        }
        return new AuthorEntity(domain.getId(), domain.getFullName());
    }
}