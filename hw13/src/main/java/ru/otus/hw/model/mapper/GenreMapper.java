package ru.otus.hw.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import ru.otus.hw.model.dto.GenreDto;
import ru.otus.hw.model.entity.Genre;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface GenreMapper {
    GenreDto toDto(Genre entity);

    Genre toEntity(GenreDto dto);
}