package ru.otus.hw.migration.item.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.hw.model.mongo.dto.AuthorDto;

import javax.sql.DataSource;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AuthorItemWriter {
    private final DataSource dataSource;

    // Insert from Java to Temp table
    @Bean
    public JdbcBatchItemWriter<AuthorDto> authorInsertTempTable() {
        final JdbcBatchItemWriter<AuthorDto> writer = new JdbcBatchItemWriter<>();

        writer.setDataSource(dataSource);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(
            "INSERT INTO temp_table_author_mongo_to_h2(id_mongo, id_h2) " +
            "VALUES (:id, nextval('seq_author_h2'))"
        );

        return writer;
    }

    // Insert from Temp table to Target table
    @Bean
    public JdbcBatchItemWriter<AuthorDto> authorJdbcBatchItemWriter() {
        final JdbcBatchItemWriter<AuthorDto> writer = new JdbcBatchItemWriter<>();

        writer.setDataSource(dataSource);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(
            "INSERT INTO authors(id, full_name) VALUES " +
            "((SELECT id_h2 FROM temp_table_author_mongo_to_h2 WHERE id_mongo = :id), :fullName)"
        );

        return writer;
    }

    // Combine steps. Java -> Temp table -> Target table
    @Bean
    public CompositeItemWriter<AuthorDto> compositeAuthorWriter(
        final JdbcBatchItemWriter<AuthorDto> authorInsertTempTable,
        final JdbcBatchItemWriter<AuthorDto> authorJdbcBatchItemWriter
    ) {
        final CompositeItemWriter<AuthorDto> writer = new CompositeItemWriter<>();

        writer.setDelegates(
            List.of(authorInsertTempTable, authorJdbcBatchItemWriter)
        );

        return writer;
    }
}
