package ru.job4j.functional.user;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Sample user conversion class to show functional interfaces.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UserConvert {

    /**
     * Applies operation to given list and returns result.
     *
     * @param names     List of names.
     * @param operation Operation to apply.
     * @return List of operation results.
     */
    public List<User> convert(List<String> names, Function<String, User> operation) {
        List<User> users = new ArrayList<>();
        names.forEach(n -> {
                    User user = operation.apply(n);
                    users.add(user);
                }
        );
        return users;
    }

    /**
     * Sample User class
     *
     * @author Aleksei Sapozhnikov (vermucht@gmail.com)
     * @version $Id$
     * @since 0.1
     */
    public static class User {
        /**
         * User name.
         */
        private final String name;

        /**
         * Constructs new User object.
         *
         * @param name User name.
         */
        public User(String name) {
            this.name = name;
        }

        /**
         * Returns object as string.
         *
         * @return String describing user object.
         */
        @Override
        public String toString() {
            return String.format("User[name=%s]", this.name);
        }
    }
}
