package sort.user;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the User class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 13.02.2018
 */
public class UserTest {

    /**
     * Test getName() method.
     */
    @Test
    public void whenGetNameThenName() {
        User user = new User("Katy", 23);
        String result = user.name();
        String expected = "Katy";
        assertThat(result, is(expected));
    }

    /**
     * Test getAge() method.
     */
    @Test
    public void whenGetAgeThenAge() {
        User user = new User("Katy", 23);
        int result = user.age();
        int expected = 23;
        assertThat(result, is(expected));
    }

    /**
     * Test compareTo() method.
     */
    @Test
    public void whenCompareEqualAgeThenZero() {
        User first = new User("Katy", 23);
        User second = new User("Angela", 23);
        int result = first.compareTo(second);
        assertThat(result, is(0));
    }

    @Test
    public void whenCompareFirstAgeLargerThenPositive() {
        User first = new User("Katy", 44);
        User second = new User("Angela", 23);
        boolean result = first.compareTo(second) > 0;
        assertThat(result, is(true));
    }

    @Test
    public void whenCompareFirstAgeSmallerThenPositive() {
        User first = new User("Katy", 12);
        User second = new User("Angela", 23);
        boolean result = first.compareTo(second) < 0;
        assertThat(result, is(true));
    }

    /**
     * Test equals() method.
     */
    @Test
    public void whenEqualElementsThenTrue() {
        User first = new User("Vasya", 12);
        User second = new User("Vasya", 12);
        boolean result = first.equals(second);
        assertThat(result, is(true));
    }

    @Test
    public void whenNotEqualOnlyNameThenFalse() {
        User first = new User("John", 32);
        User second = new User("Vasya", 32);
        boolean result = first.equals(second);
        assertThat(result, is(false));
    }

    @Test
    public void whenNotEqualOnlyAgeThenFalse() {
        User first = new User("Vasya", 21);
        User second = new User("Vasya", 12);
        boolean result = first.equals(second);
        assertThat(result, is(false));
    }

    /**
     * Test hashcode() method.
     */
    @Test
    public void whenEqualElementsThenEqualHashcode() {
        User first = new User("Vasya", 12);
        User second = new User("Vasya", 12);
        boolean result = first.hashCode() == second.hashCode();
        assertThat(result, is(true));
    }
}