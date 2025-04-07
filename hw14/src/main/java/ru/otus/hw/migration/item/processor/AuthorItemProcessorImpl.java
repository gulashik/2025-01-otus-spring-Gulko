package ru.otus.hw.migration.item.processor;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.model.mongo.dto.AuthorDto;
import ru.otus.hw.model.mongo.entity.Author;

public class AuthorItemProcessorImpl implements ItemProcessor<Author, AuthorDto> {

    @Override
    public AuthorDto process(final Author item) {
        return new AuthorDto(item.getId(), item.getFullName());
    }
}
