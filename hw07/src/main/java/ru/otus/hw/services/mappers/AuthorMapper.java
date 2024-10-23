package ru.otus.hw.services.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.entity.Author;

@Component
public class AuthorMapper {
    public AuthorDto toDto(Author entity) {
        if (entity == null) {
            return null;
        }
        return new AuthorDto(entity.getId(), entity.getFullName());
    }

    public Author toEntity(AuthorDto dto) {
        if (dto == null) {
            return null;
        }
        return new Author(dto.getId(), dto.getFullName());
    }
}