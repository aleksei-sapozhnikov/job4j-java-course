package ru.job4j.map.equals_hashcode;

/**
 * Basic user class to extend for testing equals() and hashcode().
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.03.2018
 */
public abstract class AbstractUser {

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
    private java.util.Calendar birthday;

    @Override
    public String toString() {
        return String.format(
                "[name: %s, children: %s, birthday: %s]", this.name, this.children, this.birthday
        );
    }

}


