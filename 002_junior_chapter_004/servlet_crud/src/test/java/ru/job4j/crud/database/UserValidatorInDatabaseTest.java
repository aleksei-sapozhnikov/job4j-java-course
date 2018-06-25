package ru.job4j.crud.database;

import org.junit.Test;
import ru.job4j.crud.User;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserValidatorInDatabaseTest {

    /**
     * Test Singleton and getInstance()
     */
    @Test
    public void whenGetInstanceThenTheOnlyObjectInstance() {
        UserValidatorInDatabase validator1 = UserValidatorInDatabase.getInstance();
        UserValidatorInDatabase validator2 = UserValidatorInDatabase.getInstance();
        UserValidatorInDatabase validator3 = UserValidatorInDatabase.getInstance();
        assertThat(validator1 == validator2, is(true));
        assertThat(validator1 == validator3, is(true));
    }

    /**
     * Test add(), findById(), findAll()
     */
    @Test
    public void whenAddValidUserThenHeIsInStoreAndCanFindHimById() {
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User added = new User("nameOne", "loginOne", "email@one.com", 123);
        int id = validator.add(added);
        assertThat(validator.findById(id), is(added));
        assertThat(validator.findAll()[0], is(added));
    }

    /**
     * Test add() validation
     */
    @Test
    public void whenInvalidFieldThenReturnMinusOneAndNotAdded() {
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User nameInvalid = new User(null, "login", "email@mail.com", 123);
        User loginInvalid = new User("name", null, "email@mail.com", 123);
        User emailInvalid1 = new User("name", "login", "email.com", 123);
        User emailInvalid2 = new User("name", "login", null, 123);
        assertThat(validator.add(nameInvalid), is(-1));
        assertThat(validator.add(loginInvalid), is(-1));
        assertThat(validator.add(emailInvalid1), is(-1));
        assertThat(validator.add(emailInvalid2), is(-1));
        assertThat(validator.findAll(), is(new User[0]));
    }

    /**
     * Test update()
     */
    @Test
    public void whenUpdateUserThenFieldsChange() {
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old@email.com", 123);
        int id = validator.add(old);
        User upd = new User("new_name", "new_login", "new@email.ru", 456);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, upd.getName(), upd.getLogin(), upd.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithWrongIdThenUpdateFalseAndUserNotChanging() {
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User add = new User("old_name", "old_login", "old@email.com", 123);
        int id = validator.add(add);
        int badId = id + 2134;
        User update = new User("new_name", "new_login", "new@email.ru", 456);
        assertThat(validator.update(badId, update), is(false));
        User result = validator.findById(id);
        User expected = new User(id, add.getName(), add.getLogin(), add.getEmail(), add.getCreated());
        assertThat(result, is(expected));
    }


    /**
     * Test update() validation.
     */
    @Test
    public void whenUpdateUserHasWrongEmailThenFalseAndNothingChanges() {
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old@email.com", 123);
        int id = validator.add(old);
        User update = new User("new_name", "new_login", "new_email", 456);  // wrong email
        assertThat(validator.update(id, update), is(false));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), old.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }

    /**
     * Test update() with null fields in updating user.
     */
    @Test
    public void whenUpdateUserWithOnlyNameNotNullThenNameChange() {
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old@email.com", 123);
        int id = validator.add(old);
        User upd = new User("new_name", null, null, 456);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, upd.getName(), old.getLogin(), old.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyLoginNotNullThenLoginChange() {
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old@email.com", 123);
        int id = validator.add(old);
        User upd = new User(null, "new_login", null, 456);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), upd.getLogin(), old.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }


    @Test
    public void whenUpdateUserWithOnlyEmailNotNullThenEmailChange() {
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old@email.com", 123);
        int id = validator.add(old);
        User upd = new User(null, null, "new@email.com", 456);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), upd.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithAllFieldsNullThenNothingChanged() {
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old@email.com", 123);
        int id = validator.add(old);
        User upd = new User(null, null, null, 456);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), old.getEmail(), old.getCreated());
        assertThat(result, is(expected));
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteUserThenHeIsReturnedAndNotFoundInStore() {
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User add = new User("name", "login", "email@mail.com", 123);
        int id = validator.add(add);
        User deleted = validator.delete(id);
        assertThat(deleted, is(add));
        assertThat(validator.findById(id), nullValue());
        assertThat(validator.findAll(), is(new User[0]));
    }

    @Test
    public void whenDeleteUserWithWrongIdThenReturnedNullAndUserStaysInStorage() {
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User add = new User("name", "login", "email@mail.com", 123);
        int id = validator.add(add);
        int badId = id + 123;
        User deleted = validator.delete(badId);
        assertThat(deleted, nullValue());
        assertThat(validator.findById(id), is(add));
        assertThat(validator.findAll(), is(new User[]{add}));
    }

    /**
     * Test findById()
     */
    @Test
    public void whenAddedUsersCanFindThemById() {
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User one = new User("name_1", "login_1", "email@one.ru", 123);
        User two = new User("name_2", "login_2", "email@two.ru", 456);
        User three = new User("name_3", "login_3", "email@three.ru", 789);
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
        UserValidatorInDatabase validator = UserValidatorInDatabase.getInstance();
        validator.clear();
        User one = new User("name_1", "login_1", "email@one.ru", 123);
        User two = new User("name_2", "login_2", "email@two.ru", 456);
        User three = new User("name_3", "login_3", "email@three.ru", 789);
        validator.add(one);
        validator.add(two);
        validator.add(three);
        User[] result = validator.findAll();
        User[] expected = {two, one, three};    // order shouldn't matter in assert
        assertThat(result, arrayContainingInAnyOrder(expected));
    }

}