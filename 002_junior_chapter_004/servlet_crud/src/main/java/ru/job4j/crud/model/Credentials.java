package ru.job4j.crud.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Credentials {
    private static final Logger LOG = LogManager.getLogger(Credentials.class);

    private final String login;

    private final String password;

    private final Role role;

    public Credentials(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public Role getRole() {
        return this.role;
    }

    public Credentials mergeWith(Credentials newer) {
        String login = newer.getLogin() != null ? newer.getLogin() : this.getLogin();
        String password = newer.getPassword() != null ? newer.getPassword() : this.getPassword();
        Role role = newer.getRole() != null ? newer.getRole() : this.getRole();
        return new Credentials(login, password, role);
    }

    @Override
    public String toString() {
        return String.format(
                "[credentials login=%s, password=%s, role=%s]",
                this.login, this.password, this.role
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
        Credentials that = (Credentials) o;
        return Objects.equals(this.login, that.login)
                && Objects.equals(this.password, that.password)
                && this.role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.login, this.password, this.role);
    }

    public enum Role {
        ADMIN,
        USER
    }
}
    