package ru.job4j.crud.store;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.crud.model.Credentials;
import ru.job4j.crud.model.Info;
import ru.job4j.crud.model.User;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.crud.model.Credentials.Role.ADMIN;
import static ru.job4j.crud.model.Credentials.Role.USER;

public class DatabaseStoreTest {

    private final DatabaseStore store = DatabaseStore.getInstance();

    private final User userOne = new User(32, 123L, new Credentials("login_1", "password_1", ADMIN), new Info("name_1", "e@mail.com_1", "country_1", "city_1"));
    private final User userTwo = new User(32, 123L, new Credentials("login_2", "password_2", USER), new Info("name_2", "e@mail.com_2", "country_2", "city_2"));
    private final User userThree = new User(32, 123L, new Credentials("login_3", "password_3", ADMIN), new Info("name_3", "e@mail.com_3", "country_3", "city_3"));

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
        int id = this.store.add(this.userOne);
        assertThat(this.store.findById(id), is(this.userOne));
        assertThat(this.store.findAll().get(0), is(this.userOne));
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

    /**
     * Test findAllCountries()
     */
    @Test
    public void whenFindAllCountriesThenListsOfCountries() {
        store.add(this.userOne);
        store.add(this.userTwo);
        store.add(this.userThree);
        List<String> countries = store.findAllCountries();
        assertThat(countries, containsInAnyOrder(
                this.userTwo.getInfo().getCountry(),
                this.userOne.getInfo().getCountry(),
                this.userThree.getInfo().getCountry()
        ));
    }

    /**
     * Test findAllCities()
     */
    @Test
    public void whenFindAllCitiesThenListsOfCities() {
        store.add(this.userOne);
        store.add(this.userTwo);
        store.add(this.userThree);
        assertThat(store.findAllCities(), containsInAnyOrder(
                this.userTwo.getInfo().getCity(),
                this.userOne.getInfo().getCity(),
                this.userThree.getInfo().getCity()
        ));
    }
}