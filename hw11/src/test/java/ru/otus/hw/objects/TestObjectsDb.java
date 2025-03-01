package ru.otus.hw.objects;

import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Comment;
import ru.otus.hw.models.entity.Genre;

import java.util.List;

public class TestObjectsDb {
    public static void recreateTestObjects(
        MongoTemplate mongoTemplate
    ) {
        mongoTemplate.getDb().drop();

        Author author1 = new Author("1", "Author_1");
        Author author2 = new Author("2", "Author_2");
        Author author3 = new Author("3", "Author_3");
        List.of(author1, author2, author3).forEach(mongoTemplate::save);

        Genre genre1 = new Genre("1", "Genre_1");
        Genre genre2 = new Genre("2", "Genre_2");
        Genre genre3 = new Genre("3", "Genre_3");
        List.of(genre1, genre2, genre3).forEach(mongoTemplate::save);

        Book book1 = new Book("1", "BookTitle_1", author1, genre1);
        Book book2 = new Book("2", "BookTitle_2", author2, genre2);
        Book book3 = new Book("3", "BookTitle_3", author3, genre3);
        List.of(book1, book2, book3).forEach(mongoTemplate::save);

        Comment comment_1_book_1 = new Comment("1", "Comment_1_book_1", book1);
        Comment comment_2_book_1 = new Comment("2", "Comment_2_book_1", book1);
        Comment comment_3_book_1 = new Comment("3", "Comment_3_book_1", book1);
        Comment comment_1_book_2 = new Comment("4", "Comment_1_book_2", book2);
        Comment comment_2_book_2 = new Comment("5", "Comment_2_book_2", book2);
            List.of(
                comment_1_book_1, comment_2_book_1, comment_3_book_1,
                comment_1_book_2, comment_2_book_2
            ).forEach(mongoTemplate::save);
    }
}
