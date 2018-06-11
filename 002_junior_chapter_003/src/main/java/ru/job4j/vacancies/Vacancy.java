package ru.job4j.vacancies;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.StringJoiner;

public class Vacancy {
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");

    private final int id;
    private final String theme;
    private final String url;
    private final long published;

    public Vacancy(int id, String theme, String url, long published) {
        this.id = id;
        this.theme = theme;
        this.url = url;
        this.published = published;
    }

    public Vacancy(String theme, String url, long published) {
        this(-1, theme, url, published);
    }

    public int getId() {
        return this.id;
    }

    public String getTheme() {
        return this.theme;
    }

    public String getUrl() {
        return this.url;
    }

    public long getPublished() {
        return this.published;
    }

    @Override
    public String toString() {
        return new StringJoiner(System.lineSeparator())
                .add(LocalDateTime.ofInstant(Instant.ofEpochMilli(this.published), ZONE_ID).toString())
                .add(this.theme)
                .add(this.url)
                .add("")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vacancy vacancy = (Vacancy) o;
        return published == vacancy.published
                && Objects.equals(theme, vacancy.theme)
                && Objects.equals(url, vacancy.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(theme, url, published);
    }
}
