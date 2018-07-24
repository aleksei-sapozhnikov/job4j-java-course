package ru.job4j.crud.logic;

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

public class DatabaseValidatorTest {

    private final DatabaseValidator validator = DatabaseValidator.getInstance();

    private final User userOne = new User("name_1", "login_1", "password_1", "email@mail.com_1", 123, ADMIN, "country_1", "city_1");
    private final User userTwo = new User("name_2", "login_2", "password_2", "email@mail.com_2", 123, USER, "country_2", "city_2");

    private final User userNameNull = new User(null, "login", "password", "email@mail.com", 123, ADMIN, "country", "city");
    private final User userLoginNull = new User("name", null, "password", "email@mail.com", 123, USER, "country", "city");
    private final User userPasswordNull = new User("name", "login", null, "email@mail.com", 123, ADMIN, "country", "city");
    private final User userEmailNull = new User("name", "login", "password", null, 123, USER, "country", "city");
    private final User userEmailWrongFormat = new User("name", "login", "password", "email", 123, ADMIN, "country", "city");
    private final User userRoleNull = new User("name", "login", "password", "email@mail.com", 123, null, "country", "city");


    @Before
    public void clearStore() {
        this.validator.clear();
    }


    /**
     * Test Singleton and getInstance()
     */
    @Test
    public void whenGetInstanceThenTheOnlyObjectInstance() {
        DatabaseValidator validator1 = DatabaseValidator.getInstance();
        DatabaseValidator validator2 = DatabaseValidator.getInstance();
        DatabaseValidator validator3 = DatabaseValidator.getInstance();
        assertThat(validator1 == validator2, is(true));
        assertThat(validator1 == validator3, is(true));
    }

    /**
     * Test add(), findById(), findAll()
     */
    @Test
    public void whenAddValidUserThenHeIsInStoreAndCanFindHimById() {
        int id = validator.add(this.userOne);
        assertThat(validator.findById(id), is(this.userOne));
        assertThat(validator.findAll().get(0), is(this.userOne));
    }

    /**
     * Test add() validation
     */
    @Test
    public void whenInvalidFieldThenReturnMinusOneAndNotAdded() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        assertThat(validator.add(this.userNameNull), is(-1));
        assertThat(validator.add(this.userLoginNull), is(-1));
        assertThat(validator.add(this.userPasswordNull), is(-1));
        assertThat(validator.add(this.userEmailNull), is(-1));
        assertThat(validator.add(this.userEmailWrongFormat), is(-1));
        assertThat(validator.add(this.userRoleNull), is(-1));
        assertThat(validator.findAll(), is(Collections.EMPTY_LIST));
    }

    /**
     * Test update()
     */
    @Test
    public void whenUpdateUserThenFieldsChange() {
        int id = this.validator.add(this.userOne);
        boolean updateResult = this.validator.update(id, this.userTwo);
        User expected = new User(
                id,
                this.userTwo.getName(), this.userTwo.getLogin(), this.userTwo.getPassword(), this.userTwo.getEmail(),
                this.userOne.getCreated(), // doesn't change
                this.userTwo.getRole(), this.userTwo.getCountry(), this.userTwo.getCity()
        );
        assertThat(updateResult, is(true));
        assertThat(validator.findById(id), is(expected));
    }

    @Test
    public void whenUpdateUserWithWrongIdThenUpdateFalseAndUserNotChanging() {
        int rightId = this.validator.add(this.userOne);
        int wrongId = rightId + 2134;
        boolean updateResult = validator.update(wrongId, this.userTwo);
        User expected = new User(
                rightId,
                this.userOne.getName(), this.userOne.getLogin(), this.userOne.getPassword(), this.userOne.getEmail(),
                this.userOne.getCreated(), this.userOne.getRole(), this.userOne.getCountry(), this.userOne.getCity()
        );
        assertThat(updateResult, is(false));
        assertThat(validator.findById(rightId), is(expected));
    }

    /**
     * Test update() validation.
     */
    @Test
    public void whenUpdateUserHasWrongEmailThenFalseAndNothingChanges() {
        int id = this.validator.add(this.userOne);
        boolean updateResult = this.validator.update(id, this.userEmailWrongFormat);
        User expected = new User(
                id,
                this.userOne.getName(), this.userOne.getLogin(), this.userOne.getPassword(), this.userOne.getEmail(),
                this.userOne.getCreated(), this.userOne.getRole(), this.userOne.getCountry(), this.userOne.getCity());
        assertThat(updateResult, is(false));
        assertThat(this.validator.findById(id), is(expected));
    }

    /**
     * Test update() with null fields in updating user.
     */
    @Test
    public void whenUpdateUserWithOnlyNameNotNullThenNameChange() {
        int id = this.validator.add(this.userOne);
        User upd = new User("new_name", null, null, null, 456, null, null, null);
        boolean updateResult = this.validator.update(id, upd);
        User expected = new User(
                id,
                upd.getName(), // changed
                this.userOne.getLogin(), this.userOne.getPassword(), this.userOne.getEmail(), this.userOne.getCreated(),
                this.userOne.getRole(), this.userOne.getCountry(), this.userOne.getCity()
        );
        assertThat(updateResult, is(true));
        assertThat(this.validator.findById(id), is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyLoginNotNullThenLoginChange() {
        int id = this.validator.add(this.userOne);
        User upd = new User(null, "new_login", null, null, 456, null, null, null);
        boolean updateResult = this.validator.update(id, upd);
        User expected = new User(
                id, this.userOne.getName(),
                upd.getLogin(), // changed
                this.userOne.getPassword(), this.userOne.getEmail(), this.userOne.getCreated(),
                this.userOne.getRole(), this.userOne.getCountry(), this.userOne.getCity()
        );
        assertThat(updateResult, is(true));
        assertThat(this.validator.findById(id), is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyPasswordNotNullThenPasswordChange() {
        int id = this.validator.add(this.userOne);
        User upd = new User(null, null, "new_password", null, 456, null, null, null);
        boolean updateResult = this.validator.update(id, upd);
        User expected = new User(
                id, this.userOne.getName(), this.userOne.getLogin(),
                upd.getPassword(), // changed
                this.userOne.getEmail(), this.userOne.getCreated(),
                this.userOne.getRole(), this.userOne.getCountry(), this.userOne.getCity()
        );
        assertThat(updateResult, is(true));
        assertThat(this.validator.findById(id), is(expected));
    }


    @Test
    public void whenUpdateUserWithOnlyEmailNotNullThenEmailChange() {
        int id = this.validator.add(this.userOne);
        User upd = new User(null, null, null, "new@email.com", 456, null, null, null);
        boolean updateResult = this.validator.update(id, upd);
        User expected = new User(
                id, this.userOne.getName(), this.userOne.getLogin(), this.userOne.getPassword(),
                upd.getEmail(), // changed
                this.userOne.getCreated(), this.userOne.getRole(), this.userOne.getCountry(), this.userOne.getCity()
        );
        assertThat(updateResult, is(true));
        assertThat(this.validator.findById(id), is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyRoleNotNullThenRoleChange() {
        int id = this.validator.add(this.userOne);
        User upd = new User(null, null, null, null, 456, USER, null, null);
        boolean updateResult = this.validator.update(id, upd);
        User expected = new User(
                id, this.userOne.getName(), this.userOne.getLogin(), this.userOne.getPassword(),
                this.userOne.getEmail(), this.userOne.getCreated(),
                upd.getRole(), // changed
                this.userOne.getCountry(), this.userOne.getCity()
        );
        assertThat(updateResult, is(true));
        assertThat(this.validator.findById(id), is(expected));
    }

    @Test
    public void whenUpdateUserWithAllFieldsNullThenNothingChanged() {
        int id = this.validator.add(this.userOne);
        User upd = new User(null, null, null, null, 456, null, null, null);
        boolean updateResult = this.validator.update(id, upd);
        User expected = new User(
                id, this.userOne.getName(), this.userOne.getLogin(), this.userOne.getPassword(), this.userOne.getEmail(),
                this.userOne.getCreated(), this.userOne.getRole(), this.userOne.getCountry(), this.userOne.getCity()
        );
        assertThat(updateResult, is(true));
        assertThat(this.validator.findById(id), is(expected));
    }

    /**
     * Test changing roles in update()
     */

    @Test
    public void whenAdminChangesHisRoleThenTrue() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, ADMIN, "oldCountry", "oldCity");
        User upd = new User("upd_name", "upd_login", "upd_password", "upd@email.com", 123, USER, "newCountry", "newCity");
        int id = validator.add(old);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, upd.getName(), upd.getLogin(), upd.getPassword(), upd.getEmail(),
                upd.getCreated(), upd.getRole(), upd.getCountry(), upd.getCity());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUserDoesNotChangeHisRoleThenTrue() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, USER, "oldCountry", "oldCity");
        User upd = new User("upd_name", "upd_login", "upd_password", "upd@email.com", 123, USER, "new_Country", "newCity");
        int id = validator.add(old);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, upd.getName(), upd.getLogin(), upd.getPassword(), upd.getEmail(),
                upd.getCreated(), upd.getRole(), upd.getCountry(), upd.getCity());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUserTriesToChangeHisRoleThenFalse() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, USER, "oldCountry", "oldCity");
        int id = validator.add(old);
        User upd = new User("upd_name", "upd_login", "upd_password", "upd@email.com", 123, ADMIN, "new_Country", "newCity");
        assertThat(validator.update(id, upd), is(false));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), old.getPassword(), old.getEmail(),
                old.getCreated(), old.getRole(), old.getCountry(), old.getCity());
        assertThat(result, is(expected));
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteUserThenHeIsReturnedAndNotFoundInStore() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User add = new User("name", "login", "password", "email@mail.com", 123, USER, "country", "city");
        int id = validator.add(add);
        User deleted = validator.delete(id);
        assertThat(deleted, is(add));
        assertThat(validator.findById(id), nullValue());
        assertThat(validator.findAll(), is(Collections.EMPTY_LIST));
    }

    @Test
    public void whenDeleteUserWithWrongIdThenReturnedNullAndUserStaysInStorage() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User add = new User("name", "login", "password", "email@mail.com", 123, ADMIN, "country", "city");
        int id = validator.add(add);
        int badId = id + 123;
        User deleted = validator.delete(badId);
        assertThat(deleted, nullValue());
        assertThat(validator.findById(id), is(add));
        assertThat(validator.findAll(), is(Collections.singletonList(add)));
    }

    /**
     * Test findById()
     */
    @Test
    public void whenAddedUsersCanFindThemById() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User one = new User("name_1", "login_1", "password_1", "email@one.ru", 123, ADMIN, "country_1", "city_1");
        User two = new User("name_2", "login_2", "password_2", "email@two.ru", 456, USER, "country_2", "city_2");
        User three = new User("name_3", "login_3", "password_3", "email@three.ru", 789, ADMIN, "country_3", "city_3");
        int idOne = validator.add(one);
        int idTwo = validator.add(two);
        int idThree = validator.add(three);
        assertThat(validator.findById(idTwo), is(two));
        assertThat(validator.findById(idThree), is(three));
        assertThat(validator.findById(idOne), is(one));
    }

    /**
     * Test findAll()
     */
    @Test
    public void whenAddedUsersThenFindAllReturnsThemAll() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User one = new User("name_1", "login_1", "password_1", "email@one.ru", 123, USER, "country_1", "city_1");
        User two = new User("name_2", "login_2", "password_2", "email@two.ru", 456, ADMIN, "country_2", "city_2");
        User three = new User("name_3", "login_3", "password_3", "email@three.ru", 789, USER, "country_3", "city_3");
        validator.add(one);
        validator.add(two);
        validator.add(three);
        User[] result = validator.findAll().toArray(new User[0]);
        User[] expected = {two, one, three};    // order shouldn't matter in assert
        assertThat(result, arrayContainingInAnyOrder(expected));
    }

}