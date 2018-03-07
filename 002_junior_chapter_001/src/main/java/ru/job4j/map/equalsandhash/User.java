package ru.job4j.map.equalsandhash;

import java.time.LocalDate;

/**
 * Basic user class to extend for testing equals() and hashcode().
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.03.2018
 */
class User {

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
    User(String name, int children, LocalDate birthday) {
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

}


