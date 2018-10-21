package ru.job4j.music.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

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
    public static final int DEFAULT_ID = -1;
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

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o Other object.
     * @return <tt>true</tt> if this object is the same as the obj argument; <tt>false</tt> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(name, address.name);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return Integer hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
