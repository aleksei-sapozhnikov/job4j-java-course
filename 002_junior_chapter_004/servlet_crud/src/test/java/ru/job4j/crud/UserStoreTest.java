package ru.job4j.crud;

import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserStoreTest {

    @Test
    public void whenClearTables() throws SQLException {
        UserStore store = UserStore.getInstance();
        store.dropExistingAndCreateNeededTables();
        store.add(new User(1, "vasya", "vas", "vas@gmail.com", System.currentTimeMillis()));
        store.add(new User(2, "petya", "pet", "pet@mail.ru", System.currentTimeMillis()));
        User[] result = store.findAll();
        for (User res : result) {
            System.out.println(res);
        }
    }

    @Test
    public void whenGetInstanceThenReturnsTheOnlyObject() {
        UserStore store1 = UserStore.getInstance();
        UserStore store2 = UserStore.getInstance();
        UserStore store3 = UserStore.getInstance();
        assertThat(store1 == store2, is(true));
        assertThat(store1 == store3, is(true));
    }

    @Test
    public void dropAllExistingTables() {
    }

    @Test
    public void add() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void findById() {
    }

    @Test
    public void findAll() {
    }
}