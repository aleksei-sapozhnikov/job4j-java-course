package ru.job4j.vacancies;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.StringJoiner;

public class Vacancy {
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");

    private final int id;
    private final String title;
    private final String url;
    private final long updated;

    public Vacancy(int id, String title, String url, long updated) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.updated = updated;
    }

    public Vacancy(String title, String url, long updated) {
        this(-1, title, url, updated);
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }

    public long getUpdated() {
        return this.updated;
    }

    @Override
    public String toString() {
        return new StringJoiner(System.lineSeparator())
                .add(Integer.toString(this.id)
                        .concat(" : ")
                        .concat(LocalDateTime.ofInstant(Instant.ofEpochMilli(this.updated), ZONE_ID).toString()))
                .add(this.title)
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
        return updated == vacancy.updated
                && Objects.equals(title, vacancy.title)
                && Objects.equals(url, vacancy.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, url, updated);
    }
}
