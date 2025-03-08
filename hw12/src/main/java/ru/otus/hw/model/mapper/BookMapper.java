package ru.otus.hw.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import ru.otus.hw.model.dto.BookDto;
import ru.otus.hw.model.entity.Book;

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    uses = {AuthorMapper.class, GenreMapper.class}
)
public interface BookMapper {
    @Mapping(source = "author", target = "authorDto")
    @Mapping(source = "genre", target = "genreDto")
    BookDto toDto(Book entity);

    @Mapping(source = "authorDto", target = "author")
    @Mapping(source = "genreDto", target = "genre")
    Book toEntity(BookDto dto);
}
