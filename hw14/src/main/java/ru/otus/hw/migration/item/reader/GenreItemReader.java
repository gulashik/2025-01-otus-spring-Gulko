package ru.otus.hw.migration.item.reader;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.hw.model.mongo.entity.Genre;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

import java.util.HashMap;

@RequiredArgsConstructor
@Component
public class GenreItemReader {

    private final MongoGenreRepository mongoGenreRepository;

    @Bean
    public RepositoryItemReader<Genre> genreReader() {
        return new RepositoryItemReaderBuilder<Genre>()
            .name("genreReader")
            .repository(mongoGenreRepository)
            .methodName("findAll")
            .pageSize(10)
            .sorts(new HashMap<>())
            .build();
    }
}
