package ru.job4j.generics.store;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the AbstractStore class methods.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 01.03.2018
 */
public class AbstractStoreTest {

    /**
     * Test add() and findById()
     */
    @Test
    public void whenAddThenCanBeFound() {
        UserStore users = new UserStore();
        RoleStore roles = new RoleStore();
        assertThat(users.findById("123"), is((User) null));
        assertThat(roles.findById("123"), is((Role) null));
        users.add(new User("123"));
        roles.add(new Role("123"));
        assertThat(users.findById("123"), is(new User("123")));
        assertThat(roles.findById("123"), is(new Role("123")));
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteExistingThenTrueNotFoundOld() {
        UserStore users = new UserStore();
        RoleStore roles = new RoleStore();
        users.add(new User("123"));
        roles.add(new Role("123"));
        assertThat(users.findById("123"), is(new User("123")));
        assertThat(roles.findById("123"), is(new Role("123")));
        assertThat(users.delete("123"), is(true));
        assertThat(roles.delete("123"), is(true));
        assertThat(users.findById("123"), is((User) null));
        assertThat(roles.findById("123"), is((Role) null));
    }

    @Test
    public void whenDeleteElementNotFoundThenFalse() {
        UserStore users = new UserStore();
        RoleStore roles = new RoleStore();
        users.add(new User("123"));
        roles.add(new Role("123"));
        assertThat(users.delete("456"), is(false));
        assertThat(roles.delete("456"), is(false));
    }

    /**
     * Test replace()
     */
    @Test
    public void whenReplaceThenNotFoundOldAndFoundNew() {
        UserStore users = new UserStore();
        RoleStore roles = new RoleStore();
        users.add(new User("123"));
        roles.add(new Role("123"));
        assertThat(users.findById("123"), is(new User("123")));
        assertThat(roles.findById("123"), is(new Role("123")));
        users.replace("123", new User("456"));
        roles.replace("123", new Role("456"));
        assertThat(users.findById("123"), is((User) null));
        assertThat(roles.findById("123"), is((Role) null));
        assertThat(users.findById("456"), is(new User("456")));
        assertThat(roles.findById("456"), is(new Role("456")));
    }

    @Test
    public void whenReplaceElementNotFoundThenFalse() {
        UserStore users = new UserStore();
        RoleStore roles = new RoleStore();
        users.add(new User("123"));
        roles.add(new Role("123"));
        assertThat(users.replace("456", new User("aaa")), is(false));
        assertThat(roles.replace("456", new Role("aaa")), is(false));
    }
}