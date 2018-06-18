package ru.job4j.crud;

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
}
