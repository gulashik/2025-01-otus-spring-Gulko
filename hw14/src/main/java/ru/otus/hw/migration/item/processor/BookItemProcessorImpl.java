package ru.otus.hw.migration.item.processor;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.model.mongo.dto.BookDto;
import ru.otus.hw.model.mongo.entity.Book;

public class BookItemProcessorImpl implements ItemProcessor<Book, BookDto> {

    @Override
    public BookDto process(final Book item) {
        final String authorId = item.getAuthor().getId();
        final String genreId = item.getGenre().getId();
        return new BookDto(item.getId(), item.getTitle(), authorId, genreId);
    }
}
