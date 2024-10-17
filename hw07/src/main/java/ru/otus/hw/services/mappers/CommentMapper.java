package ru.otus.hw.services.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentMapper {

    private final BookMapper bookMapper;

    public ru.otus.hw.models.dto.Comment toDomain(Comment entity) {
        if (entity == null) {
            return null;
        }
        return new ru.otus.hw.models.dto.Comment(
            entity.getId(),
            entity.getText(),
            bookMapper.toDomain(entity.getBook())
        );
    }

    public Comment toEntity(ru.otus.hw.models.dto.Comment domain) {
        if (domain == null) {
            return null;
        }
        return new Comment(
            domain.getId(),
            domain.getText(),
            bookMapper.toEntity(domain.getBook())
        );
    }
}
