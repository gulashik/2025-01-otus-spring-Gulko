package ru.otus.hw.model.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import ru.otus.hw.model.entity.Author;
import ru.otus.hw.model.entity.Book;
import ru.otus.hw.model.entity.Comment;
import ru.otus.hw.model.entity.Genre;

@Configuration
public class SpringDataRestConfig {

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            /**
             * Настраивает базовую конфигурацию репозиториев REST API.
             * Определяет, как репозитории будут представлены в виде REST-ресурсов.
             *
             * @param config Конфигурация репозиториев
             * @param cors Реестр CORS-конфигураций
             */
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
                // Включаем экспозицию ID в JSON-ответах
                // По умолчанию Spring Data REST не включает поля ID в ответы,
                config.exposeIdsFor(Author.class, Book.class, Comment.class, Genre.class);

                // Устанавливаем базовый путь API
                // Все эндпоинты Spring Data REST будут доступны по этому пути
                config.setBasePath("/api/datarest/v1");

                // CORS конфигурация
                // Определяет, какие домены, методы и заголовки разрешены для доступа к API
                cors.addMapping("/api/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET")
                    .allowedHeaders("*")
                    .allowCredentials(false)
                    .maxAge(3600);
            }
        };
    }
}