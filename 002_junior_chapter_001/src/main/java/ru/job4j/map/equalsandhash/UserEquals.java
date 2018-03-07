package ru.job4j.map.equalsandhash;

import java.time.LocalDate;
import java.util.Objects;

/**
 * User class with overriding only equals().
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.03.2018
 */
class UserEquals {

    /**
     * Name.
     */
    private String name;

    /**
     * Number of children
     */
    private int children;

    /**
     * Birth date.
     */
    private LocalDate birthday;

    /**
     * Initializes fields.
     *
     * @param name     user name.
     * @param children number of children.
     * @param birthday birth date.
     */
    UserEquals(String name, int children, LocalDate birthday) {
        this.name = name;
        this.children = children;
        this.birthday = birthday;
    }

    /**
     * Returns string with current field values.
     *
     * @return string with current field values.
     */
    @Override
    public String toString() {
        return String.format(
                "[name: %s, children: %s, birthday: %s]", this.name, this.children, this.birthday
        );
    }

    /**
     * Equals()
     *
     * @param other object to check equality to this object.
     * @return <tt>true</tt> of objects are equal, <tt>false</tt> if not.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        UserEquals that = (UserEquals) other;
        return children == that.children
                && Objects.equals(name, that.name)
                && Objects.equals(birthday, that.birthday);
    }

}


