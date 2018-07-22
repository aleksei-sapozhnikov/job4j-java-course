package ru.job4j.crud.store;

import org.junit.Test;
import ru.job4j.crud.User;

import java.util.Collections;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.crud.Role.ADMIN;
import static ru.job4j.crud.Role.USER;

public class DatabaseStoreTest {

    /**
     * Test Singleton and getInstance()
     */
    @Test
    public void whenGetInstanceThenTheOnlyObjectInstance() {
        DatabaseStore store1 = DatabaseStore.getInstance();
        DatabaseStore store2 = DatabaseStore.getInstance();
        DatabaseStore store3 = DatabaseStore.getInstance();
        assertThat(store1 == store2, is(true));
        assertThat(store1 == store3, is(true));
    }

    /**
     * Test add(), findById(), findAll()
     */
    @Test
    public void whenAddUserThenHeIsInStoreAndCanFindHimById() {
        DatabaseStore store = DatabaseStore.getInstance();
        store.clear();
        User added = new User("nameOne", "loginOne", "passwordOne", "email@one.com", 123, ADMIN, "country", "city");
        int id = store.add(added);
        assertThat(store.findById(id), is(added));
        assertThat(store.findAll().get(0), is(added));
    }

    /**
     * Test update()
     */
    @Test
    public void whenUpdateUserWithTheSameIdThenFieldsChange() {
        DatabaseStore store = DatabaseStore.getInstance();
        store.clear();
        User add = new User("old_name", "old_login", "old_password", "old_email", 123, ADMIN, "old_country", "old_city");
        int id = store.add(add);
        User upd = new User(id, "new_name", "new_login", "new_password", "new_email", 456, USER, "new_country", "new_city");
        assertThat(store.update(upd), is(true));
        User result = store.findById(id);
        User expected = new User(id, upd.getName(), upd.getLogin(), upd.getPassword(), upd.getEmail(),
                add.getCreated(), upd.getRole(), upd.getCountry(), upd.getCity());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithWrongIdThenUpdateFalseAndUserNotChanging() {
        DatabaseStore store = DatabaseStore.getInstance();
        store.clear();
        User add = new User("old_name", "old_login", "old_password", "old_email", 123, ADMIN, "country", "city");
        int id = store.add(add);
        int badId = id + 2134;
        User update = new User(badId, "new_name", "new_login", "new_password", "new_email", 456, USER, "country", "city");
        assertThat(store.update(update), is(false));
        User result = store.findById(id);
        User expected = new User(id, add.getName(), add.getLogin(), add.getPassword(), add.getEmail(),
                add.getCreated(), add.getRole(), add.getCountry(), add.getCity());
        assertThat(result, is(expected));
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteUserThenHeIsReturnedAndNotFoundInStore() {
        DatabaseStore store = DatabaseStore.getInstance();
        store.clear();
        User add = new User("name", "login", "password", "email", 123, ADMIN, "country", "city");
        int id = store.add(add);
        User deleted = store.delete(id);
        assertThat(deleted, is(add));
        assertThat(store.findById(id), nullValue());
        assertThat(store.findAll(), is(Collections.EMPTY_LIST));
    }

    @Test
    public void whenDeleteUserWithWrongIdThenFalseAndUserStays() {
        DatabaseStore store = DatabaseStore.getInstance();
        store.clear();
        User add = new User("name", "login", "password", "email", 123, USER, "country", "city");
        int id = store.add(add);
        int badId = id + 123;
        User deleted = store.delete(badId);
        assertThat(deleted, nullValue());
        assertThat(store.findById(id), is(add));
        assertThat(store.findAll(), is(Collections.singletonList(add)));
    }

    /**
     * Test findById()
     */
    @Test
    public void whenAddedUsersCanFindThemById() {
        DatabaseStore store = DatabaseStore.getInstance();
        store.clear();
        User one = new User("name_1", "login_1", "password_1", "email_1", 123, ADMIN, "country_1", "city_1");
        User two = new User("name_2", "login_2", "password_2", "email_2", 456, USER, "country_2", "city_2");
        User three = new User("name_3", "login_3", "password_3", "email_3", 789, ADMIN, "country_3", "city_3");
        int idOne = store.add(one);
        int idTwo = store.add(two);
        int idThree = store.add(three);
        assertThat(store.findById(idTwo), is(two));
        assertThat(store.findById(idThree), is(three));
        assertThat(store.findById(idOne), is(one));
    }

    /**
     * Test findAll()
     */
    @Test
    public void whenAddedUsersThenFindAllReturnsThemAll() {
        DatabaseStore store = DatabaseStore.getInstance();
        store.clear();
        User one = new User("name_1", "login_1", "password_1", "email_1", 123, USER, "country_1", "city_1");
        User two = new User("name_2", "login_2", "password_2", "email_2", 456, ADMIN, "country_2", "city");
        User three = new User("name_3", "login_3", "password_3", "email_3", 789, USER, "country_3", "city");
        store.add(one);
        store.add(two);
        store.add(three);
        User[] result = store.findAll().toArray(new User[0]);
        User[] expected = {two, one, three};    // order shouldn't matter in assert
        assertThat(result, arrayContainingInAnyOrder(expected));
    }
}