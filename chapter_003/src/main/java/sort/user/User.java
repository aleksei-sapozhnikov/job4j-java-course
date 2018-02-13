package sort.user;

import java.util.Objects;

/**
 * Users of some system.
 * For sorting exercises.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 13.02.2018
 */
public class User implements Comparable<Integer> {

    /**
     * User name.
     */
    private String name;

    /**
     * User age.
     */
    private int age;

    /**
     * Constructor.
     *
     * @param name User name.
     * @param age  User age.
     */
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * Compare two user objects by age.
     *
     * @param other Object to compare to.
     * @return a negative integer, zero, or a positive integer as this User object
     * is less than, equal to, or greater than the specified User object.
     */
    @Override
    public int compareTo(Integer other) {
        return this.age - other;
    }

    /**
     * Check if this User object is equal to another object.
     *
     * @param o Object to compare to.
     * @return true or false as this User object is equal to another object or not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return age == user.age
                && Objects.equals(name, user.name);
    }

    /**
     * Return hashcode for the User object.
     *
     * @return integer hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
