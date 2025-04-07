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
import ru.otus.hw.migration.item.processor.CommentItemProcessorImpl;
import ru.otus.hw.model.mongo.dto.CommentDto;
import ru.otus.hw.model.mongo.entity.Comment;

import javax.sql.DataSource;

import static ru.otus.hw.migration.job.Job.CHUNK_SIZE;

@RequiredArgsConstructor
@Component
public class CommentStep {
    private final DataSource dataSource;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public TaskletStep createTempTableComment() {
        return new StepBuilder("createTempTableComment", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                        "CREATE TABLE temp_table_comment_mongo_to_h2 " +
                            "(id_mongo VARCHAR(255) NOT NULL UNIQUE, id_h2 BIGINT NOT NULL UNIQUE)"
                    );
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }

    @Bean
    public TaskletStep createTempSeqComment() {
        return new StepBuilder("createTempSeqComment", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute("CREATE SEQUENCE IF NOT EXISTS seq_comment_h2");
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }

    @Bean
    public Step migrationCommentStep(
        final RepositoryItemReader<Comment> reader,
        final CompositeItemWriter<CommentDto> writer,
        final CommentItemProcessorImpl processor
    ) {
        return new StepBuilder("migrationCommentStep", jobRepository)
            .<Comment, CommentDto>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public TaskletStep dropTempTableComment() {
        return new StepBuilder("dropTempTableComment", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                        "DROP TABLE temp_table_comment_mongo_to_h2"
                    );
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }

    @Bean
    public TaskletStep dropTempSeqComment() {
        return new StepBuilder("dropTempSeqComment", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource)
                        .execute("DROP SEQUENCE IF EXISTS seq_comment_h2");
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }
}
