package ru.job4j.map.equalshashcode;

import java.time.LocalDate;

/**
 * User class where methods equals() and hashcode() are not overridden.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.03.2018
 */
class UserNoEqualsNoHashcode extends AbstractUser {

    /**
     * Constructs new User instance.
     *
     * @param name     user name.
     * @param children number of children.
     * @param birthday birth date.
     */
    UserNoEqualsNoHashcode(String name, int children, LocalDate birthday) {
        super(name, children, birthday);
    }

}
