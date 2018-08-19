package ru.job4j.music.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Address entity.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Address {
    /**
     * Default id if one was not specified.
     */
    private static final int DEFAULT_ID = -1;
    /**
     * Empty address - to use instead of null object.
     */
    public static final Address EMPTY_ADDRESS = new Address(DEFAULT_ID, "");
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Address.class);
    /**
     * Unique id.
     */
    private final int id;
    /**
     * Text describing address.
     */
    private final String name;

    /**
     * Constructs new object.
     *
     * @param id   Unique id.
     * @param name Text describing address.
     */
    public Address(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Constructs new object with default id.
     *
     * @param name Text describing address.
     */
    public Address(String name) {
        this(DEFAULT_ID, name);
    }

    /**
     * Returns id.
     *
     * @return Value of id field.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns new object with fields same as of this object
     * but with given id.
     *
     * @param id Id to set to the object.
     * @return New object with given id.
     */
    public Address setId(int id) {
        return new Address(id, this.name);
    }

    /**
     * Returns name.
     *
     * @return Value of name field.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return String.format(
                "[address id=%s, name=%s]",
                this.id,
                this.name
        );
    }
}
