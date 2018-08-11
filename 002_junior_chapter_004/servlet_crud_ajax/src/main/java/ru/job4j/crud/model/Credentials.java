package ru.job4j.crud.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Holds information about user credentials - different fields.
 * Credentials can be accessed by getters.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class Credentials {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Credentials.class);
    /**
     * User login.
     */
    private final String login;
    /**
     * User password.
     */
    private final String password;
    /**
     * User role in the system.
     */
    private final Role role;

    /**
     * Constructs new object.
     *
     * @param login    User login.
     * @param password User password.
     * @param role     User role in the system.
     */
    public Credentials(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    /**
     * Returns user login.
     *
     * @return User login.
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
     * Returns user role in the system.
     *
     * @return User role in the system.
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * Merges this object with newer one. Checks every field. If the field in newer
     * object is not null, then replaces older value from the newer object.
     * Else - leaves the value from the older object.
     *
     * @param newer Newer object.
     * @return Updated object.
     */
    public Credentials mergeWith(Credentials newer) {
        String login = newer.getLogin() != null ? newer.getLogin() : this.getLogin();
        String password = newer.getPassword() != null ? newer.getPassword() : this.getPassword();
        Role role = newer.getRole() != null ? newer.getRole() : this.getRole();
        return new Credentials(login, password, role);
    }

    /**
     * Returns string representing the object.
     *
     * @return String representing the object.
     */
    @Override
    public String toString() {
        return String.format(
                "[credentials login=%s, password=%s, role=%s]",
                this.login, this.password, this.role
        );
    }

    /**
     * Check equality of this object and other object.
     *
     * @param o Other object.
     * @return <tt>true</tt> if objects are equal, <tt>false</tt> if not.
     */
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

    /**
     * Returns integer hashcode of the object.
     *
     * @return Hashcode of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.login, this.password, this.role);
    }

    public enum Role {
        ADMIN,
        USER
    }
}
    