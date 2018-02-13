package convert.user;

import java.util.Objects;

/**
 * Users of some system.
 * For conversion exercises.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 13.02.2018
 */
public class User {

    /**
     * User unique identifier.
     */
    private int id;

    /**
     * User name.
     */
    private String name;

    /**
     * City where the User is living.
     */
    private String city;

    /**
     * Constructor.
     *
     * @param id   Unique identifier.
     * @param name Name of the user.
     * @param city City where the user is living.
     */
    User(int id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    /**
     * Get id.
     *
     * @return User id field value.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Checks if two User objects are equal.
     *
     * @param other another object to compare with.
     * @return true if objects are equal, false if not.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        User user = (User) other;
        return Objects.equals(this.id, user.id)
                && Objects.equals(this.name, user.name)
                && Objects.equals(this.city, user.city);
    }

    /**
     * Generates hashcode using object's fields.
     *
     * @return int hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, city);
    }
}
