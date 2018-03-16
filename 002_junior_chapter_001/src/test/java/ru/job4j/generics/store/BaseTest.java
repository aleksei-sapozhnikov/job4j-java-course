package ru.job4j.generics.store;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the Base class methods.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 01.03.2018
 */
public class BaseTest {

    /**
     * Test id()
     */
    @Test
    public void whenGetIdTheId() {
        User user = new User("123");
        Role role = new Role("456");
        assertThat(user.id(), is("123"));
        assertThat(role.id(), is("456"));
    }

    @Test
    public void whenIdEqualThenTrue() {
        User user1 = new User("123");
        User user2 = new User("123");
        Role role1 = new Role("456");
        Role role2 = new Role("456");
        assertThat(user1.equals(user2), is(true));
        assertThat(role1.equals(role2), is(true));
    }

    @Test
    public void whenIdNotEqualThenFalse() {
        User user1 = new User("123");
        User user2 = new User("321");
        Role role1 = new Role("456");
        Role role2 = new Role("654");
        assertThat(user1.equals(user2), is(false));
        assertThat(role1.equals(role2), is(false));
    }

    @Test
    public void whenTheSameObjectThenEqualsTrue() {
        User user = new User("123");
        Role role = new Role("456");
        assertThat(user.equals(user), is(true));
        assertThat(role.equals(role), is(true));
    }

    @Test
    public void whenNullOrOtherObjectThenEqualsFalse() {
        User user = new User("123");
        String notUser = "User";
        Role role = new Role("456");
        String notRole = "Role";
        assertThat(user.equals(notUser), is(false));
        assertThat(user.equals(null), is(false));
        assertThat(role.equals(role), is(true));
        assertThat(role.equals(null), is(false));
    }

    @Test
    public void whenEqualObjectsThenHashCodesAreTheSame() {
        User user1 = new User("123");
        User user2 = new User("123");
        Role role1 = new Role("456");
        Role role2 = new Role("456");
        assertThat(user1.hashCode() == user2.hashCode(), is(true));
        assertThat(role1.hashCode() == role2.hashCode(), is(true));
    }
}