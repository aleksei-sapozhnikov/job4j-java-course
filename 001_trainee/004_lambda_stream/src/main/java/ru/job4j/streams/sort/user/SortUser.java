package ru.job4j.streams.sort.user;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class to sort objects of class User in collections.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 13.02.2018
 */
public class SortUser {

    /**
     * Convert list of users into a sorted set.
     * Sorting by compareTo() method of User class.
     *
     * @return Sorted TreeSet of User objects.
     */
    public Set<User> sort(List<User> users) {
        return new TreeSet<>(users);
    }

    /**
     * Sort users by their name length.
     *
     * @param users List of users to sort.
     * @return ArrayList of users sorted by name length.
     */
    public List<User> sortNameLength(List<User> users) {
        //let's make anonymous class
        users.sort(new Comparator<User>() {
            @Override
            public int compare(User left, User right) {
                return Integer.compare(
                        left.name().length(), right.name().length()
                );
            }
        });
        return users;
    }

    /**
     * Sort users by their name and age.
     * First, sorted by name (String) (lexicographically).
     * If names are equal, then sorted by age (int)
     *
     * @param users List of users to sort.
     * @return Sorted list of users.
     */
    public List<User> sortByAllFields(List<User> users) {
        //let's make it short
        users.sort(Comparator
                .comparing(User::name)
                .thenComparingInt(User::age));
        return users;
    }
}
