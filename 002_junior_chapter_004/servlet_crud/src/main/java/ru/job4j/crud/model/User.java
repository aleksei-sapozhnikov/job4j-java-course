package ru.job4j.crud.model;

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
     * Time when the user was created.
     */
    private final long created;
    /**
     * User credentials for the system.
     */
    private final Credentials credentials;
    /**
     * Information about the user.
     */
    private final Info info;

    /**
     * Constructs new object with.
     *
     * @param id          Unique id of the user.
     * @param created     Time when the user was created.
     * @param credentials Credentials of the new user.
     * @param info        Info of the new user.
     */
    public User(int id, long created, Credentials credentials, Info info) {
        this.id = id;
        this.created = created;
        this.credentials = credentials;
        this.info = info;
    }

    /**
     * Constructs new object with default id (-1) and creation time (current time).
     *
     * @param credentials Credentials of the new user.
     * @param info        Info of the new user.
     */
    public User(Credentials credentials, Info info) {
        this(-1, System.currentTimeMillis(), credentials, info);
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
     * Returns time when the user was created.
     *
     * @return Time of user creation.
     */
    public long getCreated() {
        return this.created;
    }

    /**
     * Returns credentials.
     *
     * @return Credentials.
     */
    public Credentials getCredentials() {
        return this.credentials;
    }

    /**
     * Returns user info.
     *
     * @return Info.
     */
    public Info getInfo() {
        return this.info;
    }

    /**
     * Returns new user object with all fields the same as of this
     * but with given id.
     *
     * @param newId New id.
     * @return New user object same as given but with new id.
     */
    public User changeId(int newId) {
        return new User(newId, this.created, this.credentials, this.info);
    }

    /**
     * Returns string describing current user object.
     *
     * @return String describing current user object.
     */
    @Override
    public String toString() {
        return String.format(
                "[user id=%s, created=%s, %s, %s]",
                this.id,
                Instant.ofEpochMilli(this.created).atZone(ZoneId.systemDefault()),
                this.credentials, this.info
        );
    }

    /**
     * Returns <tt>true</tt> if this user is equal to the given object.
     * Takes in account all fields except id given by database and date of creation.
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
        return Objects.equals(this.credentials, that.credentials)
                && Objects.equals(this.info, that.info);
    }

    /**
     * Calculates integer hashcode().
     *
     * @return Integer hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.credentials, this.info);
    }
}
