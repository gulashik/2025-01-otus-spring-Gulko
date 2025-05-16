package ru.otus.hw.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.model.entity.Book;

import java.util.List;

@RepositoryRestResource(
    path = "books",
    collectionResourceDescription = @Description("Коллекция книг"),
    itemResourceDescription = @Description("Книга")
)
public interface BookRepository extends JpaRepository<Book, Long> {

    @RestResource(
        description = @Description("Получение списка всех книг")
    )
    @EntityGraph("BookEntity-author-genre")
    @NotNull
    List<Book> findAll();

    @RestResource(
        path = "byTitle",
        description = @Description("Поиск книг по названию")
    )
    List<Book> findByTitleContainingIgnoreCase(@Param("title") String title);

    @RestResource(
        path = "byAuthor",
        description = @Description("Поиск книг по автору")
    )
    @EntityGraph("BookEntity-author-genre")
    List<Book> findByAuthorFullNameContainingIgnoreCase(@Param("authorName") String authorName);

    @RestResource(
        path = "byGenre",
        description = @Description("Поиск книг по жанру")
    )
    @EntityGraph("BookEntity-author-genre")
    List<Book> findByGenreName(@Param("genreName") String genreName);
}