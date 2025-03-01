package ru.otus.hw.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.entity.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    @NotNull
    Mono<Book> findById(@NotNull String id);

    @NotNull
    Flux<Book> findAll();
}
