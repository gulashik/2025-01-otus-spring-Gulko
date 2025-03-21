package ru.otus.hw.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import ru.otus.hw.model.dto.AuthorDto;
import ru.otus.hw.model.entity.Author;


@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AuthorMapper {
    AuthorDto toDto(Author author);

    Author toEntity(AuthorDto authorDto);
}