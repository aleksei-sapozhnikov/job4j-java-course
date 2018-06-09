package ru.job4j.vacancies;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class Vacancy {
    private final String theme;
    private final String url;
    private final LocalDateTime published;

    public Vacancy(String theme, String url, LocalDateTime published) {
        this.theme = theme;
        this.url = url;
        this.published = published;
    }

    @Override
    public String toString() {
        return new StringJoiner(System.lineSeparator())
                .add(this.published.toString())
                .add(this.theme)
                .add(this.url)
                .add("")
                .toString();
    }

}
