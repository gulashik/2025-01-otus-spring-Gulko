package ru.otus.hw.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    @NotNull
    Optional<Book> findById(@NotNull String id);

    @NotNull
    List<Book> findAll();
}
