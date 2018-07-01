package ru.job4j.crud;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

/**
 * Simple user object to store in the "User storage".
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class User {
    /**
     * Unique id.
     */
    private final int id;
    /**
     * Name.
     */
    private final String name;
    /**
     * Login in the system.
     */
    private final String login;
    /**
     * Email.
     */
    private final String email;
    /**
     * Date when this user was created in milliseconds.
     */
    private final long created;
    /**
     * User password to enter storage system.
     */
    private final String password;

    /**
     * Constructs new User object.
     *
     * @param id       Unique id.
     * @param name     User name.
     * @param login    Login in the system.
     * @param password Password for user to enter system.
     * @param email    User email.
     * @param created  Date of creation in milliseconds.
     */
    public User(int id, String name, String login, String password, String email, long created) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.created = created;
    }

    /**
     * Constructs new User object.
     *
     * @param id      Unique id.
     * @param name    User name.
     * @param login   Login in the system.
     * @param email   User email.
     * @param created Date of creation in milliseconds.
     */
    public User(int id, String name, String login, String email, long created) {
        this(id, name, login, "", email, created);
    }

    /**
     * Constructs new User object with default id = -1 and password="".
     *
     * @param name    User name.
     * @param login   Login in the system.
     * @param email   User email.
     * @param created Date of creation in milliseconds.
     */
    public User(String name, String login, String email, long created) {
        this(-1, name, login, email, created);
    }

    /**
     * Constructs new User object with default id = -1 and password="".
     *
     * @param name     User name.
     * @param login    Login in the system.
     * @param password Password for user to enter system.
     * @param email    User email.
     * @param created  Date of creation in milliseconds.
     */
    public User(String name, String login, String password, String email, long created) {
        this(-1, name, login, password, email, created);
    }

    /**
     * Returns user id.
     *
     * @return User id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns user name.
     *
     * @return User name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns user system login.
     *
     * @return User system login.
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * Returns user password.
     *
     * @return User password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns user email.
     *
     * @return User email.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Returns date when the user was created.
     *
     * @return Date when the user was created.
     */
    public long getCreated() {
        return this.created;
    }

    /**
     * Returns string describing current user object.
     *
     * @return String describing current user object.
     */
    @Override
    public String toString() {
        return String.format(
                "[user id = %s, name = %s, login = %s, email = %s, created = %s]",
                this.id, this.name, this.login, this.email,
                Instant.ofEpochMilli(this.created).atZone(ZoneId.systemDefault())
        );
    }

    /**
     * Returns <tt>true</tt> if this user is equal to the given object.
     * Takes in account all fields except id given by database.
     *
     * @param object Given object to compare with.
     * @return <tt>true</tt> if this User is equal to object, <tt>false</tt> if not.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        User that = (User) object;
        return this.created == that.created
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.login, that.login)
                && Objects.equals(this.email, that.email);
    }

    /**
     * Calculates integer hashcode().
     *
     * @return Integer hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, login, email, created);
    }
}
