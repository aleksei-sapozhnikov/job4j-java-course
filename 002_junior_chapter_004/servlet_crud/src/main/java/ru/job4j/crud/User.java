package ru.job4j.crud;

import java.time.Instant;
import java.time.ZoneId;

public class User {
    private final int id;
    private final String name;
    private final String login;
    private final String email;
    private final long created;

    public User(int id, String name, String login, String email, long created) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.email = email;
        this.created = created;
    }

    public User(String name, String login, String email, long created) {
        this(-1, name, login, email, created);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getLogin() {
        return this.login;
    }

    public String getEmail() {
        return this.email;
    }

    public long getCreated() {
        return this.created;
    }

    @Override
    public String toString() {
        return String.format(
                "[user id = %s, name = %s, login = %s, email = %s, created = %s]",
                this.id, this.name, this.login, this.email,
                Instant.ofEpochMilli(this.created).atZone(ZoneId.systemDefault())
        );
    }
}
