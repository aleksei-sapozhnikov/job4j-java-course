package ru.job4j.crud;

import org.junit.Test;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserValidatorTest {

    /**
     * Test Singleton and getInstance()
     */
    @Test
    public void whenGetInstanceThenTheOnlyObjectInstance() {
        UserValidator validator1 = UserValidator.getInstance();
        UserValidator validator2 = UserValidator.getInstance();
        UserValidator validator3 = UserValidator.getInstance();
        assertThat(validator1 == validator2, is(true));
        assertThat(validator1 == validator3, is(true));
    }

    /**
     * Test add(), findById(), findAll()
     */
    @Test
    public void whenAddValidUserThenHeIsInStoreAndCanFindHimById() {
        UserValidator validator = UserValidator.getInstance();
        validator.clearExistingStructureAndCreateAgain();
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
        UserValidator validator = UserValidator.getInstance();
        validator.clearExistingStructureAndCreateAgain();
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
        UserValidator validator = UserValidator.getInstance();
        validator.clearExistingStructureAndCreateAgain();
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
        UserValidator validator = UserValidator.getInstance();
        validator.clearExistingStructureAndCreateAgain();
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
        UserValidator validator = UserValidator.getInstance();
        validator.clearExistingStructureAndCreateAgain();
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
        UserValidator validator = UserValidator.getInstance();
        validator.clearExistingStructureAndCreateAgain();
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
        UserValidator validator = UserValidator.getInstance();
        validator.clearExistingStructureAndCreateAgain();
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
        UserValidator validator = UserValidator.getInstance();
        validator.clearExistingStructureAndCreateAgain();
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
        UserValidator validator = UserValidator.getInstance();
        validator.clearExistingStructureAndCreateAgain();
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
        UserValidator validator = UserValidator.getInstance();
        validator.clearExistingStructureAndCreateAgain();
        User add = new User("name", "login", "email@mail.com", 123);
        int id = validator.add(add);
        User deleted = validator.delete(id);
        assertThat(deleted, is(add));
        assertThat(validator.findById(id), nullValue());
        assertThat(validator.findAll(), is(new User[0]));
    }

    @Test
    public void whenDeleteUserWithWrongIdThenReturnedNullAndUserStaysInStorage() {
        UserValidator validator = UserValidator.getInstance();
        validator.clearExistingStructureAndCreateAgain();
        User add = new User("name", "login", "email@mail.com", 123);
        int id = validator.add(add);
        int badId = id + 123;
        User deleted = validator.delete(badId);
        assertThat(deleted, nullValue());
        assertThat(validator.findById(id), is(add));
        assertThat(validator.findAll(), is(new User[]{add}));
    }

}