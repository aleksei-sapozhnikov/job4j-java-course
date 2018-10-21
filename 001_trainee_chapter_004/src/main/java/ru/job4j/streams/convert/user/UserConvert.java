package ru.job4j.streams.convert.user;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Conversion of Collections with User objects.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 13.02.2018
 */
public class UserConvert {

    /**
     * Returns a map of Users.
     *
     * @param list List of Users.
     * @return Map with key == User.id and value == User object.
     */
    public Map<Integer, User> process(List<User> list) {
        return list.stream()
                .collect(Collectors.toMap(User::getId, user -> user));
    }
}
