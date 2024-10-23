package ru.otus.hw.services.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.entity.Book;

@RequiredArgsConstructor
@Component
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookDto toDto(Book entity) {
        if (entity == null) {
            return null;
        }

        var author = authorMapper.toDto(entity.getAuthor());
        var genre = genreMapper.toDto(entity.getGenre());

        return new BookDto(
            entity.getId(),
            entity.getTitle(),
            author,
            genre
        );
    }

    public Book toEntity(BookDto dto) {
        if (dto == null) {
            return null;
        }
        var authorEntity = authorMapper.toEntity(dto.getAuthorDto());
        var genreEntity = genreMapper.toEntity(dto.getGenreDto());

        return new Book(
            dto.getId(),
            dto.getTitle(),
            authorEntity,
            genreEntity
        );
    }
}
