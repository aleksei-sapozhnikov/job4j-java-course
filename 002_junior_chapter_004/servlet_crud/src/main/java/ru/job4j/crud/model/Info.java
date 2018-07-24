package ru.job4j.crud.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Info {
    private final Logger log = LogManager.getLogger(Info.class);

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
}
    