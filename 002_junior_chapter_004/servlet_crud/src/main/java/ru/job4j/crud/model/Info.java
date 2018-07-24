package ru.job4j.crud.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Info {
    private static final Logger LOG = LogManager.getLogger(Info.class);

    private final String name;

    private final String email;

    private final String country;

    private final String city;

    public Info(String name, String email, String country, String city) {
        this.name = name;
        this.email = email;
        this.country = country;
        this.city = city;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getCountry() {
        return this.country;
    }

    public String getCity() {
        return this.city;
    }

    @Override
    public String toString() {
        return String.format(
                "[info name=%s, email=%s, country=%s, city=%s]",
                this.name, this.email, this.country, this.city
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Info that = (Info) o;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.email, that.email) &&
                Objects.equals(this.country, that.country) &&
                Objects.equals(this.city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.email, this.country, this.city);
    }
}
    