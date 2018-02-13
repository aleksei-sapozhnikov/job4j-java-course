package sort.user;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the SortUser class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 13.02.2018
 */
public class SortUserTest {

    /**
     * Test sort() method.
     */
    @Test
    public void whenListOfUsersThenSetSortedByAge() {
        List<User> list = new ArrayList<>(Arrays.asList(
                new User("Jan", 78),
                new User("Smith", 22),
                new User("Colin", 32),
                new User("Zebra", 2),
                new User("Garry", 120)
        ));
        User[] result = new SortUser().sort(list).toArray(new User[list.size()]);
        User[] expected = new User[]{
                new User("Zebra", 2),
                new User("Smith", 22),
                new User("Colin", 32),
                new User("Jan", 78),
                new User("Garry", 120)
        };
        assertThat(result, is(expected));
    }

    @Test
    public void whenEmptyListOfUsersThenEmptySet() {
        List<User> list = new ArrayList<>();
        User[] result = new SortUser().sort(list).toArray(new User[list.size()]);
        User[] expected = new User[]{};
        assertThat(result, is(expected));
    }

}