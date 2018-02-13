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
public class User implements Comparable<User> {

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
     * Check if this User object is equal to another object.
     *
     * @param o Other object.
     * @return true or false, if objects are equal or not.
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
     * Returns hashcode
     *
     * @return Integer hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    /**
     * Compare two user objects by age.
     *
     * @param o Object to compare to.
     * @return a negative integer, zero, or a positive integer as this User object
     * is less than, equal to, or greater than the specified User object.
     */
    @Override
    public int compareTo(User o) {
        return Integer.compare(this.age, o.age);
    }
}
