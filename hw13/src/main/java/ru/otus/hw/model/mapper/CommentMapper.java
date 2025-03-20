package ru.otus.hw.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import ru.otus.hw.model.dto.CommentDto;
import ru.otus.hw.model.entity.Comment;

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    uses = {BookMapper.class}
)
public interface CommentMapper {
    @Mapping(source = "book", target = "bookDto")
    CommentDto toDto(Comment entity);

    @Mapping(source = "bookDto", target = "book")
    Comment toEntity(CommentDto dto);
}