package ru.job4j.storage.user;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for UserStorage class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.04.2018
 */
public class UserStorageTest {

    /**
     * Test add().
     */
    @Test
    public void whenAddUserThenItIsInStorage() {
        UserStorage storage = new UserStorage();
        storage.add(new User(12, 43));
        assertThat(storage.add(new User(12, 444)), is(false)); // reject same id
        assertThat(storage.findById(12).amount(), is(43)); // find by id
    }

    /**
     * Test update().
     */
    @Test
    public void whenUpdateUserThenChangesAmount() {
        UserStorage storage = new UserStorage();
        storage.add(new User(12, 43));
        assertThat(storage.update(new User(44, 32)), is(false)); // not existing
        assertThat(storage.update(new User(12, 33)), is(true)); // exists
        assertThat(storage.findById(12).amount(), is(33)); // amount changed
    }

    /**
     * Test findById().
     */
    @Test
    public void whenFindByIdThenUserOrNull() {
        UserStorage storage = new UserStorage();
        storage.add(new User(12, 43));
        assertThat(storage.findById(12).amount(), is(43)); // existing
        assertThat(storage.findById(33), is((User) null)); // not existing
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteThenUserNotFound() {
        UserStorage storage = new UserStorage();
        storage.add(new User(12, 43));
        assertThat(storage.delete(new User(44, 32)), is(false)); // not found
        assertThat(storage.delete(new User(12, 32)), is(true)); // found, only id matters
        assertThat(storage.findById(12), is((User) null)); // deleted
    }

    /**
     * Test transfer()
     */
    @Test
    public void whenTransferThenAmountsChange() {
        UserStorage storage = new UserStorage();
        storage.add(new User(1, 50)); // "from"
        storage.add(new User(2, 25)); // "to"
        assertThat(storage.transfer(1, 2, 100), is(false)); // "from: doesn't have so much
        assertThat(storage.findById(1).amount(), is(50)); // not changed
        assertThat(storage.findById(2).amount(), is(25)); // not changed
        assertThat(storage.transfer(1, 2, 11), is(true)); // that's ok
        assertThat(storage.findById(1).amount(), is(39)); // changed
        assertThat(storage.findById(2).amount(), is(36)); // changed
        assertThat(storage.transfer(1, 2, 39), is(true)); // "from: transfers all his amount
        assertThat(storage.findById(1).amount(), is(0)); // changed
        assertThat(storage.findById(2).amount(), is(75)); // changed
    }
}