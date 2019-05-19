package ru.job4j.createbeans.util;

/**
 * User object.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class User {
    /**
     * Unique id.
     */
    private long id;
    /**
     * Username.
     */
    private final String name;

    /**
     * Constructs new object.
     *
     * @param name User name.
     */
    public User(String name) {
        this.name = name;
    }

    /**
     * Returns id.
     *
     * @return Value of id field.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Sets id value.
     *
     * @param id Value to set.
     */
    public User setId(long id) {
        this.id = id;
        return this;
    }

    /**
     * Returns name.
     *
     * @return Value of name field.
     */
    public String getName() {
        return this.name;
    }
}
