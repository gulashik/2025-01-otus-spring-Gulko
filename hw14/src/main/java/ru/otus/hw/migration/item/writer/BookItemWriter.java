package ru.otus.hw.migration.item.writer;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.hw.model.sourcedb.dto.BookDto;

import javax.sql.DataSource;
import java.util.List;

@Component
public class BookItemWriter {
    private final DataSource dataSource;

    public BookItemWriter(
        @Qualifier("postgresDataSource") DataSource dataSource
    ) {
        this.dataSource = dataSource;
    }

    // Insert from Java to Temp table
    @Bean
    public JdbcBatchItemWriter<BookDto> bookInsertTempTable() {
        final JdbcBatchItemWriter<BookDto> writer = new JdbcBatchItemWriter<>();

        writer.setDataSource(dataSource);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        writer.setSql(
            "INSERT INTO temp_table_book_mongo_to_h2(id_mongo, id_h2) " +
                "VALUES (:id, nextval('seq_book_h2'))");

        return writer;
    }

    // Insert from Temp table to Target table
    @Bean
    public JdbcBatchItemWriter<BookDto> bookJdbcBatchItemWriter() {
        final JdbcBatchItemWriter<BookDto> writer = new JdbcBatchItemWriter<>();

        writer.setItemPreparedStatementSetter(
            (bookDto, statement) -> {
                statement.setString(1, bookDto.getTitle());
                statement.setString(2, bookDto.getId());
                statement.setString(3, bookDto.getAuthorId());
                statement.setString(4, bookDto.getGenreId());
            }
        );
        writer.setSql(
            "INSERT INTO books(title, id, author_id, genre_id) " +
            "VALUES (?, " +
            "(SELECT id_h2 FROM temp_table_book_mongo_to_h2 WHERE id_mongo = ?), " +
            "(SELECT id_h2 FROM temp_table_author_mongo_to_h2 WHERE id_mongo = ?), " +
            "(SELECT id_h2 FROM temp_table_genre_mongo_to_h2 WHERE id_mongo = ?)" +
            ")"
        );
        writer.setDataSource(dataSource);

        return writer;
    }

    // Combine steps. Java -> Temp table -> Target table
    @Bean
    public CompositeItemWriter<BookDto> compositeBookWriter(
        final JdbcBatchItemWriter<BookDto> bookInsertTempTable,
        final JdbcBatchItemWriter<BookDto> bookJdbcBatchItemWriter
    ) {
        final CompositeItemWriter<BookDto> writer = new CompositeItemWriter<>();

        writer.setDelegates(
            List.of(bookInsertTempTable, bookJdbcBatchItemWriter)
        );

        return writer;
    }
}
