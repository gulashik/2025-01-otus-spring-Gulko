package ru.otus.hw.migration.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.migration.item.processor.BookItemProcessorImpl;
import ru.otus.hw.model.mongo.dto.BookDto;
import ru.otus.hw.model.mongo.entity.Book;

import javax.sql.DataSource;

import static ru.otus.hw.migration.job.Job.CHUNK_SIZE;

@RequiredArgsConstructor
@Component
public class BookStep {
    private final DataSource dataSource;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public TaskletStep createTempTableBook() {
        return new StepBuilder("createTempTableBook", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                        "CREATE TABLE IF NOT EXISTS temp_table_book_mongo_to_h2 " +
                            "(id_mongo VARCHAR(255) NOT NULL UNIQUE, id_h2 BIGINT NOT NULL UNIQUE)"
                    );
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }

    @Bean
    public Step migrationBookStep(
        final RepositoryItemReader<Book> reader,
        final CompositeItemWriter<BookDto> writer,
        final BookItemProcessorImpl processor
    ) {
        return new StepBuilder("migrationBookStep", jobRepository)
            .<Book, BookDto>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public TaskletStep createTempSeqBook() {
        return new StepBuilder("createTempSeqBook", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("CREATE SEQUENCE IF NOT EXISTS seq_book_h2");
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }

    @Bean
    public TaskletStep dropTempTableBook() {
        return new StepBuilder("dropTempTableBook", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource)
                        .execute("DROP TABLE temp_table_book_mongo_to_h2");
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }

    @Bean
    public TaskletStep dropTempSeqBook() {
        return new StepBuilder("dropTempSeqBook", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource)
                        .execute("DROP SEQUENCE IF EXISTS seq_book_h2");
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }
}
