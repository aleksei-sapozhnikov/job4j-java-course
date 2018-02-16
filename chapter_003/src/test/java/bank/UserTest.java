package bank;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the User class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
public class UserTest {

    /**
     * Test passport() method.
     */
    @Test
    public void whenPassportThenPassportValue() {
        User user = new User("123-45-67", "Vasya");
        String result = user.passport();
        String expected = "123-45-67";
        assertThat(result, is(expected));
    }

    /**
     * Test equals() method.
     */
    @Test
    public void whenEqualUsersThenTrue() {
        User left = new User("123-45-67", "Vasya");
        User right = new User("123-45-67", "Vasya");
        boolean result = left.equals(right);
        assertThat(result, is(true));
    }

    @Test
    public void whenNotEqualNameThenFalse() {
        User left = new User("123-45-67", "Inna");
        User right = new User("123-45-67", "Vasya");
        boolean result = left.equals(right);
        assertThat(result, is(false));
    }

    @Test
    public void whenNotEqualPassportThenFalse() {
        User left = new User("123-45-67", "Vasya");
        User right = new User("678-90-12", "Vasya");
        boolean result = left.equals(right);
        assertThat(result, is(false));
    }

    @Test
    public void whenComparedToItselfThenTrue() {
        User user = new User("123-45-67", "Vasya");
        boolean result = user.equals(user);
        assertThat(result, is(true));
    }

    @Test
    public void whenComparedToOtherClassThenFalse() {
        User user = new User("123-45-67", "Vasya");
        String str = "User";
        boolean result = user.equals(str);
        assertThat(result, is(false));
    }

    @Test
    public void whenComparedToNullThenFalse() {
        User user = new User("123-45-67", "Vasya");
        String str = null;
        boolean result = user.equals(null);
        assertThat(result, is(false));
    }

    /**
     * Test hashCode() method.
     */
    @Test
    public void whenEqualObjectsThenTheSameHashCode() {
        User left = new User("123-45-67", "Vasya");
        User right = new User("123-45-67", "Vasya");
        boolean result = left.hashCode() == right.hashCode();
        assertThat(result, is(true));
    }
}