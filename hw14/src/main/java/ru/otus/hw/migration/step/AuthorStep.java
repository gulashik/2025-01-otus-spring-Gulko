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
import ru.otus.hw.migration.item.processor.AuthorItemProcessorImpl;
import ru.otus.hw.model.mongo.dto.AuthorDto;
import ru.otus.hw.model.mongo.entity.Author;

import javax.sql.DataSource;

import static ru.otus.hw.migration.job.Job.CHUNK_SIZE;

@RequiredArgsConstructor
@Component
public class AuthorStep {

    private final DataSource dataSource;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public TaskletStep createTempTableAuthor() {
        return new StepBuilder("createTempTableAuthor", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                        "CREATE TABLE IF NOT EXISTS temp_table_author_mongo_to_h2 " +
                            "(id_mongo VARCHAR(255) NOT NULL UNIQUE, id_h2 BIGINT NOT NULL UNIQUE)"
                    );
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }

    @Bean
    public TaskletStep dropTempTableAuthor() {
        return new StepBuilder("dropTempTableAuthor", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource)
                        .execute("DROP TABLE temp_table_author_mongo_to_h2");
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }

    @Bean
    public TaskletStep createTempSeqAuthor() {
        return new StepBuilder("createTempSeqAuthor", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("CREATE SEQUENCE IF NOT EXISTS seq_author_h2");
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }

    @Bean
    public TaskletStep dropTempSeqAuthor() {
        return new StepBuilder("dropTempSeqAuthor", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource)
                        .execute("DROP SEQUENCE IF EXISTS seq_author_h2");
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }

    @Bean
    public Step migrationAuthorStep(
        final RepositoryItemReader<Author> reader,
        final CompositeItemWriter<AuthorDto> writer,
        final AuthorItemProcessorImpl processor
    ) {
        return new StepBuilder("migrationAuthorStep", jobRepository)
            .<Author, AuthorDto>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .allowStartIfComplete(true)
            .build();
    }
}
