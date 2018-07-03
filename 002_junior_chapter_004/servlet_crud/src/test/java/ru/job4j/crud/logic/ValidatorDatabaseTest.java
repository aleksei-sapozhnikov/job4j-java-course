package ru.job4j.crud.logic;

import org.junit.Test;
import ru.job4j.crud.User;

import java.util.Collections;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ValidatorDatabaseTest {

    /**
     * Test Singleton and getInstance()
     */
    @Test
    public void whenGetInstanceThenTheOnlyObjectInstance() {
        ValidatorDatabase validator1 = ValidatorDatabase.getInstance();
        ValidatorDatabase validator2 = ValidatorDatabase.getInstance();
        ValidatorDatabase validator3 = ValidatorDatabase.getInstance();
        assertThat(validator1 == validator2, is(true));
        assertThat(validator1 == validator3, is(true));
    }

    /**
     * Test add(), findById(), findAll()
     */
    @Test
    public void whenAddValidUserThenHeIsInStoreAndCanFindHimById() {
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User added = new User("nameOne", "loginOne", "passwordOne", "email@one.com", 123);
        int id = validator.add(added);
        assertThat(validator.findById(id), is(added));
        assertThat(validator.findAll().get(0), is(added));
    }

    /**
     * Test add() validation
     */
    @Test
    public void whenInvalidFieldThenReturnMinusOneAndNotAdded() {
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User nameInvalid = new User(null, "login", "password", "email@mail.com", 123);
        User loginInvalid = new User("name", null, "password", "email@mail.com", 123);
        User passwordInvalid = new User("name", "login", null, "email@mail.com", 123);
        User emailInvalid1 = new User("name", "login", "password", "email.com", 123);
        User emailInvalid2 = new User("name", "login", "password", null, 123);
        assertThat(validator.add(nameInvalid), is(-1));
        assertThat(validator.add(loginInvalid), is(-1));
        assertThat(validator.add(emailInvalid1), is(-1));
        assertThat(validator.add(emailInvalid2), is(-1));
        assertThat(validator.findAll(), is(Collections.EMPTY_LIST));
    }

    /**
     * Test update()
     */
    @Test
    public void whenUpdateUserThenFieldsChange() {
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123);
        int id = validator.add(old);
        User upd = new User("new_name", "new_login", "new_password", "new@email.ru", 456);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, upd.getName(), upd.getLogin(), upd.getPassword(), upd.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithWrongIdThenUpdateFalseAndUserNotChanging() {
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User add = new User("old_name", "old_login", "old_password", "old@email.com", 123);
        int id = validator.add(add);
        int badId = id + 2134;
        User update = new User("new_name", "new_login", "new_password", "new@email.ru", 456);
        assertThat(validator.update(badId, update), is(false));
        User result = validator.findById(id);
        User expected = new User(id, add.getName(), add.getLogin(), add.getPassword(), add.getEmail(), add.getCreated());
        assertThat(result, is(expected));
    }


    /**
     * Test update() validation.
     */
    @Test
    public void whenUpdateUserHasWrongEmailThenFalseAndNothingChanges() {
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123);
        int id = validator.add(old);
        User update = new User("new_name", "new_login", "new_password", "new_email", 456);  // wrong email
        assertThat(validator.update(id, update), is(false));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), old.getPassword(), old.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }

    /**
     * Test update() with null fields in updating user.
     */
    @Test
    public void whenUpdateUserWithOnlyNameNotNullThenNameChange() {
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123);
        int id = validator.add(old);
        User upd = new User("new_name", null, null, null, 456);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, upd.getName(), old.getLogin(), old.getPassword(), old.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyLoginNotNullThenLoginChange() {
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123);
        int id = validator.add(old);
        User upd = new User(null, "new_login", null, null, 456);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), upd.getLogin(), old.getPassword(), old.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyPasswordNotNullThenPasswordChange() {
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123);
        int id = validator.add(old);
        User upd = new User(null, null, "new_password", null, 456);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), upd.getPassword(), old.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }


    @Test
    public void whenUpdateUserWithOnlyEmailNotNullThenEmailChange() {
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123);
        int id = validator.add(old);
        User upd = new User(null, null, null, "new@email.com", 456);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), old.getPassword(), upd.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithAllFieldsNullThenNothingChanged() {
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123);
        int id = validator.add(old);
        User upd = new User(null, null, null, null, 456);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), old.getPassword(), old.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteUserThenHeIsReturnedAndNotFoundInStore() {
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User add = new User("name", "login", "password", "email@mail.com", 123);
        int id = validator.add(add);
        User deleted = validator.delete(id);
        assertThat(deleted, is(add));
        assertThat(validator.findById(id), nullValue());
        assertThat(validator.findAll(), is(Collections.EMPTY_LIST));
    }

    @Test
    public void whenDeleteUserWithWrongIdThenReturnedNullAndUserStaysInStorage() {
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User add = new User("name", "login", "password", "email@mail.com", 123);
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
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User one = new User("name_1", "login_1", "password_1", "email@one.ru", 123);
        User two = new User("name_2", "login_2", "password_2", "email@two.ru", 456);
        User three = new User("name_3", "login_3", "password_3", "email@three.ru", 789);
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
        ValidatorDatabase validator = ValidatorDatabase.getInstance();
        validator.clear();
        User one = new User("name_1", "login_1", "password_1", "email@one.ru", 123);
        User two = new User("name_2", "login_2", "password_2", "email@two.ru", 456);
        User three = new User("name_3", "login_3", "password_3", "email@three.ru", 789);
        validator.add(one);
        validator.add(two);
        validator.add(three);
        User[] result = validator.findAll().toArray(new User[0]);
        User[] expected = {two, one, three};    // order shouldn't matter in assert
        assertThat(result, arrayContainingInAnyOrder(expected));
    }

}