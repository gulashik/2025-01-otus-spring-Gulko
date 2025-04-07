package ru.otus.hw.migration.item.processor;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.model.mongo.dto.CommentDto;
import ru.otus.hw.model.mongo.entity.Comment;

public class CommentItemProcessorImpl implements ItemProcessor<Comment, CommentDto> {

    @Override
    public CommentDto process(final Comment item) {
        return new CommentDto(item.getId(), item.getCommentText(), item.getBook().getId());
    }
}
