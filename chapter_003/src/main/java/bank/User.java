package bank;

import java.util.Objects;

/**
 * Bank user.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
public class User {

    /**
     * User passport number.
     * That is the unique user identifier.
     */
    private String passport;

    /**
     * User name.
     */
    private String name;

    /**
     * Constructor
     *
     * @param passport user passport (identifier).
     * @param name     user name.
     */
    User(String passport, String name) {
        this.passport = passport;
        this.name = name;
    }

    /**
     * Get passport information.
     *
     * @return user passport information (identifier).
     */
    public String passport() {
        return this.passport;
    }

    /**
     * Equals or not.
     *
     * @param other another object.
     * @return true of false as our object is equal to other or not.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        User user = (User) other;
        return Objects.equals(this.name, user.name)
                && Objects.equals(this.passport, user.passport);
    }

    /**
     * Hashcode.
     *
     * @return integer hashcode.
     */
    @Override
    public int hashCode() {

        return Objects.hash(this.name, this.passport);
    }
}
