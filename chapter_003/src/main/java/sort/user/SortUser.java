package sort.user;

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
     *
     * @return Sorted TreeSet of User objects.
     */
    public Set<User> sort(List<User> users) {
        return new TreeSet<>(users);
    }
}
