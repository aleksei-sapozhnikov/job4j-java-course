package convert.user;

import java.util.HashMap;
import java.util.List;

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
    public HashMap<Integer, User> process(List<User> list) {
        HashMap<Integer, User> result = new HashMap<>();
        list.forEach(user -> result.put(user.getId(), user));
        return result;
    }
}
