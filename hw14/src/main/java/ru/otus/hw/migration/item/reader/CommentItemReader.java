package ru.otus.hw.migration.item.reader;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.hw.model.mongo.entity.Comment;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;

import java.util.HashMap;

@RequiredArgsConstructor
@Component
public class CommentItemReader {

    private final MongoCommentRepository mongoCommentRepository;

    @Bean
    public RepositoryItemReader<Comment> commentReader() {
        return new RepositoryItemReaderBuilder<Comment>()
            .name("commentReader")
            .repository(mongoCommentRepository)
            .methodName("findAll")
            .pageSize(10)
            .sorts(new HashMap<>())
            .build();
    }
}
