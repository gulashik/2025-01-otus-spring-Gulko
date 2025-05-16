package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.model.entity.Comment;

import java.util.List;

@RepositoryRestResource(
    path = "comments",
    collectionResourceRel = "comments",
    collectionResourceDescription = @Description("Коллекция комментариев"),
    itemResourceDescription = @Description("Комментарий")
)
public interface CommentRepository extends JpaRepository<Comment, Long> {
   
   @RestResource(
       path = "byBook",
       rel = "findByBook", 
       description = @Description("Поиск комментариев по идентификатору книги")
   )
   List<Comment> findAllByBookId(long bookId);
}