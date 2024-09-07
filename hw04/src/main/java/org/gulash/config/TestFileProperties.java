package org.gulash.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@ConfigurationProperties(prefix = "test-file")
public record TestFileProperties(
    String name,

    int skipLines,

    int rightAnswersCountToPass,

    String locale,

    Question question,

    Answer answer
)  {
    public Locale getLocale() {
        return Locale.forLanguageTag(locale);
    }

    public record Question(String tag) {}

    public record Answer(String tag, String splitter) {}
}