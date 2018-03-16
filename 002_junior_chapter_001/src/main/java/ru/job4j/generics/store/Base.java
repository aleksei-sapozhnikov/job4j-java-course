package ru.job4j.generics.store;

import java.util.Objects;

/**
 * Base abstract object for storing.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 01.03.2018
 */
public abstract class Base {

    /**
     * Unique id.
     */
    private final String id;

    /**
     * @param id object's unique id.
     */
    Base(final String id) {
        this.id = id;
    }

    /**
     * Get id.
     *
     * @return id field value.
     */
    public String id() {
        return this.id;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param other the reference object with which to compare.
     * @return {@code true} if this object is the same as the other
     * argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Base base = (Base) other;
        return Objects.equals(this.id, base.id);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
