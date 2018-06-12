package ru.job4j.vacancies;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Vacancy object containing information about one vacancy.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 08.06.2018
 */
public class Vacancy {
    /**
     * Zone id of the "sql.ru" web resource.
     */
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");
    /**
     * Vacancy id.
     */
    private final int id;
    /**
     * Vacancy title.
     */
    private final String title;
    /**
     * Url leading to the http page with Vacancy description and polemics.
     */
    private final String url;
    /**
     * Time when the vacancy was last time updated on web-site.
     */
    private final long updated;

    /**
     * Constructs new Vacancy object.
     *
     * @param id      Vacancy id.
     * @param title   Vacancy title.
     * @param url     Vacancy http page.
     * @param updated Last time the vacancy was updated on web-site.
     */
    public Vacancy(int id, String title, String url, long updated) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.updated = updated;
    }

    /**
     * Constructs new Vacancy object with default id = -1.
     *
     * @param title   Vacancy title.
     * @param url     Vacancy http page.
     * @param updated Last time the vacancy was updated on web-site.
     */
    public Vacancy(String title, String url, long updated) {
        this(-1, title, url, updated);
    }

    /**
     * Returns vacancy id.
     *
     * @return Vacancy id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns vacancy title.
     *
     * @return Vacancy title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns vacancy url.
     *
     * @return Vacancy url.
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Returns vacancy last update time on web-site.
     *
     * @return Vacancy last update time on web-site.
     */
    public long getUpdated() {
        return this.updated;
    }

    /**
     * Returns string showing current vacancy fields values.
     *
     * @return string showing current vacancy fields values.
     */
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

    /**
     * Checks if given object is equal to this vacancy.
     *
     * @param other Given object.
     * @return <tt>true</tt> if objects are equal, Mtt<false>if not</false>.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Vacancy vacancy = (Vacancy) other;
        return this.updated == vacancy.updated
                && Objects.equals(this.title, vacancy.title)
                && Objects.equals(this.url, vacancy.url);
    }

    /**
     * Returns current vacancy hashcode.
     *
     * @return Current vacancy hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, url, updated);
    }
}
