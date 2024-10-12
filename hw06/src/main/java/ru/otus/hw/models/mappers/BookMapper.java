package ru.otus.hw.models.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.domains.Book;
import ru.otus.hw.models.entities.BookEntity;

@RequiredArgsConstructor
@Component
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public Book toDomain(BookEntity entity) {
        if (entity == null) {
            return null;
        }

        var author = authorMapper.toDomain(entity.getAuthor());
        var genre = genreMapper.toDomain(entity.getGenre());

        return new Book(
            entity.getId(),
            entity.getTitle(),
            author,
            genre
        );
    }

    public BookEntity toEntity(Book domain) {
        if (domain == null) {
            return null;
        }
        var authorEntity = authorMapper.toEntity(domain.getAuthor());
        var genreEntity = genreMapper.toEntity(domain.getGenre());

        return new BookEntity(
            domain.getId(),
            domain.getTitle(),
            authorEntity,
            genreEntity
        );
    }
}
