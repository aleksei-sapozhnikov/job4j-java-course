package ru.job4j.crud.store;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.crud.model.User;

import java.util.Collections;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.crud.model.Role.ADMIN;
import static ru.job4j.crud.model.Role.USER;

public class DatabaseStoreTest {

    private final DatabaseStore store = DatabaseStore.getInstance();

    private final User userOne = new User("name_1", "login_1", "password_1", "email@mail.com_1", 123, ADMIN, "country_1", "city_1");
    private final User userTwo = new User("name_2", "login_2", "password_2", "email@mail.com_2", 123, USER, "country_2", "city_2");
    private final User userThree = new User("name_3", "login_3", "password_3", "email@mail.com_3", 123, ADMIN, "country_3", "city_3");

    @Before
    public void clearStore() {
        this.store.clear();
    }

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
        User added = this.userOne;
        int id = this.store.add(added);
        assertThat(this.store.findById(id), is(added));
        assertThat(this.store.findAll().get(0), is(added));
    }

    /**
     * Test update()
     */
    @Test
    public void whenUpdateUserWithTheSameIdThenFieldsChange() {
        int databaseId = this.store.add(this.userOne);
        boolean updateResult = this.store.update(this.userTwo.changeId(databaseId));
        assertThat(updateResult, is(true));
        assertThat(this.store.findById(databaseId), is(this.userTwo));
    }

    @Test
    public void whenUpdateUserWithWrongIdThenUpdateFalseAndUserNotChanging() {
        int rightId = this.store.add(this.userOne);
        int wrongId = rightId + 2134;
        assertThat(this.store.update(this.userTwo.changeId(wrongId)), is(false));
        assertThat(this.store.findById(rightId), is(this.userOne));
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteUserThenHeIsReturnedAndNotFoundInStore() {
        int id = store.add(this.userOne);
        assertThat(store.delete(id), is(this.userOne));
        assertThat(store.findById(id), nullValue());
        assertThat(store.findAll(), is(Collections.EMPTY_LIST));
    }

    @Test
    public void whenDeleteUserWithWrongIdThenFalseAndUserStays() {
        int rightId = store.add(this.userOne);
        int wrongId = rightId + 123;
        assertThat(store.delete(wrongId), nullValue());
        assertThat(store.findById(rightId), is(this.userOne));
        assertThat(store.findAll(), is(Collections.singletonList(this.userOne)));
    }

    /**
     * Test findById()
     */
    @Test
    public void whenAddedUsersCanFindThemById() {
        int idOne = store.add(this.userOne);
        int idTwo = store.add(this.userTwo);
        int idThree = store.add(this.userThree);
        assertThat(store.findById(idTwo), is(this.userTwo));
        assertThat(store.findById(idThree), is(this.userThree));
        assertThat(store.findById(idOne), is(this.userOne));
    }

    /**
     * Test findAll()
     */
    @Test
    public void whenAddedUsersThenFindAllReturnsThemAll() {
        store.add(this.userOne);
        store.add(this.userTwo);
        store.add(this.userThree);
        User[] result = store.findAll().toArray(new User[0]);
        User[] expected = {this.userTwo, this.userOne, this.userThree};    // order shouldn't matter in assert
        assertThat(result, arrayContainingInAnyOrder(expected));
    }
}