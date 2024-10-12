package ru.otus.hw.models.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.domains.Comment;
import ru.otus.hw.models.entities.CommentEntity;

@RequiredArgsConstructor
@Component
public class CommentMapper {

    private final BookMapper bookMapper;

    public Comment toDomain(CommentEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Comment(
            entity.getId(),
            entity.getText(),
            bookMapper.toDomain(entity.getBook())
        );
    }

    public CommentEntity toEntity(Comment domain) {
        if (domain == null) {
            return null;
        }
        return new CommentEntity(
            domain.getId(),
            domain.getText(),
            bookMapper.toEntity(domain.getBook())
        );
    }
}
