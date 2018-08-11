package ru.job4j.crud.logic;

import org.junit.Test;
import ru.job4j.crud.User;

import java.util.Collections;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.crud.Role.ADMIN;
import static ru.job4j.crud.Role.USER;

public class DatabaseValidatorTest {

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
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User added = new User("nameOne", "loginOne", "passwordOne", "email@one.com", 123, ADMIN);
        int id = validator.add(added);
        assertThat(validator.findById(id), is(added));
        assertThat(validator.findAll().get(0), is(added));
    }

    /**
     * Test add() validation
     */
    @Test
    public void whenInvalidFieldThenReturnMinusOneAndNotAdded() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User nameInvalid = new User(null, "login", "password", "email@mail.com", 123, ADMIN);
        User loginInvalid = new User("name", null, "password", "email@mail.com", 123, USER);
        User passwordInvalid = new User("name", "login", null, "email@mail.com", 123, ADMIN);
        User emailInvalid1 = new User("name", "login", "password", "email.com", 123, USER);
        User emailInvalid2 = new User("name", "login", "password", null, 123, ADMIN);
        User roleInvalid = new User("name", "login", "password", "email@mail.com", 123, null);
        assertThat(validator.add(nameInvalid), is(-1));
        assertThat(validator.add(loginInvalid), is(-1));
        assertThat(validator.add(passwordInvalid), is(-1));
        assertThat(validator.add(emailInvalid1), is(-1));
        assertThat(validator.add(emailInvalid2), is(-1));
        assertThat(validator.add(roleInvalid), is(-1));
        assertThat(validator.findAll(), is(Collections.EMPTY_LIST));
    }

    /**
     * Test update()
     */
    @Test
    public void whenUpdateUserThenFieldsChange() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, ADMIN);
        int id = validator.add(old);
        User upd = new User("new_name", "new_login", "new_password", "new@email.ru", 456, USER);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, upd.getName(), upd.getLogin(), upd.getPassword(), upd.getEmail(), old.getCreated(), upd.getRole());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithWrongIdThenUpdateFalseAndUserNotChanging() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User add = new User("old_name", "old_login", "old_password", "old@email.com", 123, ADMIN);
        int id = validator.add(add);
        int badId = id + 2134;
        User update = new User("new_name", "new_login", "new_password", "new@email.ru", 456, USER);
        assertThat(validator.update(badId, update), is(false));
        User result = validator.findById(id);
        User expected = new User(id, add.getName(), add.getLogin(), add.getPassword(), add.getEmail(), add.getCreated(), add.getRole());
        assertThat(result, is(expected));
    }


    /**
     * Test update() validation.
     */
    @Test
    public void whenUpdateUserHasWrongEmailThenFalseAndNothingChanges() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, ADMIN);
        int id = validator.add(old);
        User update = new User("new_name", "new_login", "new_password", "new_email", 456, USER);  // wrong email
        assertThat(validator.update(id, update), is(false));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), old.getPassword(), old.getEmail(), old.getCreated(), old.getRole());
        assertThat(result, is(expected));
    }

    /**
     * Test update() with null fields in updating user.
     */
    @Test
    public void whenUpdateUserWithOnlyNameNotNullThenNameChange() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, USER);
        int id = validator.add(old);
        User upd = new User("new_name", null, null, null, 456, null);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, upd.getName(), old.getLogin(), old.getPassword(), old.getEmail(), old.getCreated(), old.getRole());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyLoginNotNullThenLoginChange() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, ADMIN);
        int id = validator.add(old);
        User upd = new User(null, "new_login", null, null, 456, null);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), upd.getLogin(), old.getPassword(), old.getEmail(), old.getCreated(), old.getRole());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyPasswordNotNullThenPasswordChange() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, USER);
        int id = validator.add(old);
        User upd = new User(null, null, "new_password", null, 456, null);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), upd.getPassword(), old.getEmail(), old.getCreated(), old.getRole());
        assertThat(result, is(expected));
    }


    @Test
    public void whenUpdateUserWithOnlyEmailNotNullThenEmailChange() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, ADMIN);
        int id = validator.add(old);
        User upd = new User(null, null, null, "new@email.com", 456, null);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), old.getPassword(), upd.getEmail(), old.getCreated(), old.getRole());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyRoleNotNullThenRoleChange() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, ADMIN);
        int id = validator.add(old);
        User upd = new User(null, null, null, null, 456, USER);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), old.getPassword(), old.getEmail(), old.getCreated(), upd.getRole());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUpdateUserWithAllFieldsNullThenNothingChanged() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, ADMIN);
        int id = validator.add(old);
        User upd = new User(null, null, null, null, 456, null);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), old.getPassword(), old.getEmail(), old.getCreated(), old.getRole());
        assertThat(result, is(expected));
    }

    /**
     * Test changing roles in update()
     */

    @Test
    public void whenAdminChangesHisRoleThenTrue() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, ADMIN);
        User upd = new User("upd_name", "upd_login", "upd_password", "upd@email.com", 123, USER);
        int id = validator.add(old);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, upd.getName(), upd.getLogin(), upd.getPassword(), upd.getEmail(), upd.getCreated(), upd.getRole());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUserDoesNotChangeHisRoleThenTrue() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, USER);
        User upd = new User("upd_name", "upd_login", "upd_password", "upd@email.com", 123, USER);
        int id = validator.add(old);
        assertThat(validator.update(id, upd), is(true));
        User result = validator.findById(id);
        User expected = new User(id, upd.getName(), upd.getLogin(), upd.getPassword(), upd.getEmail(), upd.getCreated(), upd.getRole());
        assertThat(result, is(expected));
    }

    @Test
    public void whenUserTriesToChangeHisRoleThenFalse() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User old = new User("old_name", "old_login", "old_password", "old@email.com", 123, USER);
        int id = validator.add(old);
        User upd = new User("upd_name", "upd_login", "upd_password", "upd@email.com", 123, ADMIN);
        assertThat(validator.update(id, upd), is(false));
        User result = validator.findById(id);
        User expected = new User(id, old.getName(), old.getLogin(), old.getPassword(), old.getEmail(), old.getCreated(), old.getRole());
        assertThat(result, is(expected));
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteUserThenHeIsReturnedAndNotFoundInStore() {
        DatabaseValidator validator = DatabaseValidator.getInstance();
        validator.clear();
        User add = new User("name", "login", "password", "email@mail.com", 123, USER);
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
        User add = new User("name", "login", "password", "email@mail.com", 123, ADMIN);
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
        User one = new User("name_1", "login_1", "password_1", "email@one.ru", 123, ADMIN);
        User two = new User("name_2", "login_2", "password_2", "email@two.ru", 456, USER);
        User three = new User("name_3", "login_3", "password_3", "email@three.ru", 789, ADMIN);
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
        User one = new User("name_1", "login_1", "password_1", "email@one.ru", 123, USER);
        User two = new User("name_2", "login_2", "password_2", "email@two.ru", 456, ADMIN);
        User three = new User("name_3", "login_3", "password_3", "email@three.ru", 789, USER);
        validator.add(one);
        validator.add(two);
        validator.add(three);
        User[] result = validator.findAll().toArray(new User[0]);
        User[] expected = {two, one, three};    // order shouldn't matter in assert
        assertThat(result, arrayContainingInAnyOrder(expected));
    }

}