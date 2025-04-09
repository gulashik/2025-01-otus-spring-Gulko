package ru.otus.hw.migration.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Component
public class AllTruncateStep {

    private final DataSource dataSource;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public TaskletStep truncateTargetTables() {
        return new StepBuilder("truncateTargetTables", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(
                (contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                        "SET REFERENTIAL_INTEGRITY FALSE;\n" +
                            "BEGIN TRANSACTION;\n" +
                            "DELETE AUTHORS CASCADE;\n" +
                            "DELETE BOOKS CASCADE;\n" +
                            "DELETE COMMENTS  CASCADE;\n" +
                            "DELETE GENRES CASCADE;\n" +
                            "COMMIT;\n" +
                            "SET REFERENTIAL_INTEGRITY TRUE;"
                    );
                    return RepeatStatus.FINISHED;
                }
                , platformTransactionManager
            )
            .build();
    }
}
