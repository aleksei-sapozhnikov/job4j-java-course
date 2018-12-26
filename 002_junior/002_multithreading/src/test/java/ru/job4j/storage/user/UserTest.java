package ru.job4j.storage.user;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for User class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.04.2018
 */
public class UserTest {

    /**
     * Test getters.
     */
    @Test
    public void whenGetterThenValue() {
        User user = new User(18, 434);
        assertThat(user.id(), is(18));
        assertThat(user.amount(), is(434));
    }

    /**
     * Test equals() and hashcode()
     */
    @Test
    public void whenEqualIdThenUsersEqualAndHashCodeTheSame() {
        User left = new User(1, 5);
        User right1 = new User(1, 5);
        User right2 = new User(1, 12);
        User right3 = left;
        assertThat(left.equals(right1), is(true));
        assertThat(left.equals(right2), is(true));
        assertThat(left.equals(right3), is(true));
        assertThat(left.hashCode() == right1.hashCode(), is(true));
        assertThat(left.hashCode() == right2.hashCode(), is(true));
        assertThat(left.hashCode() == right3.hashCode(), is(true));

    }

    @Test
    public void whenNotEqualObjectsThenFalse() {
        User user = new User(1, 2);
        User other1 = new User(5, 2);
        User other2 = new User(8, 56);
        User other3 = null;
        String other4 = "User";
        assertThat(user.equals(other1), is(false));
        assertThat(user.equals(other2), is(false));
        assertThat(user.equals(other3), is(false));
        assertThat(user.equals(other4), is(false));
    }
}