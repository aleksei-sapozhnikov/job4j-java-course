package ru.job4j.storage.user;

import net.jcip.annotations.GuardedBy;

import java.util.Objects;

/**
 * User for storage.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.04.2018
 */
class User {
    /**
     * User unique id.
     */
    private final int id;
    /**
     * This user's money.
     */
    @GuardedBy("this")
    private final int amount;

    /**
     * Constructs new user.
     *
     * @param id     user's unique id.
     * @param amount user's money.
     */
    User(final int id, final int amount) {
        this.id = id;
        this.amount = amount;
    }

    /**
     * Returns id.
     *
     * @return user's id field value.
     */
    public int id() {
        return this.id;
    }

    /**
     * Returns amount.
     *
     * @return user's amount field value.
     */
    public synchronized int amount() {
        return this.amount;
    }

    /**
     * Checks if this user object is equal to another object.
     *
     * @param other another object.
     * @return <tt>true</tt> if objects are equal, <tt>false</tt> if not.
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        User user = (User) other;
        return this.id == user.id;
    }

    /**
     * Returns hashcode.
     *
     * @return integer hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
