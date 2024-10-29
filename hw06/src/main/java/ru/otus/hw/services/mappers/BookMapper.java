package ru.otus.hw.services.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.models.Book;

@RequiredArgsConstructor
@Component
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public ru.otus.hw.models.dto.Book toDomain(Book entity) {
        if (entity == null) {
            return null;
        }

        var author = authorMapper.toDomain(entity.getAuthor());
        var genre = genreMapper.toDomain(entity.getGenre());

        return new ru.otus.hw.models.dto.Book(
            entity.getId(),
            entity.getTitle(),
            author,
            genre
        );
    }

    public Book toEntity(ru.otus.hw.models.dto.Book domain) {
        if (domain == null) {
            return null;
        }
        var authorEntity = authorMapper.toEntity(domain.getAuthor());
        var genreEntity = genreMapper.toEntity(domain.getGenre());

        return new Book(
            domain.getId(),
            domain.getTitle(),
            authorEntity,
            genreEntity
        );
    }
}
