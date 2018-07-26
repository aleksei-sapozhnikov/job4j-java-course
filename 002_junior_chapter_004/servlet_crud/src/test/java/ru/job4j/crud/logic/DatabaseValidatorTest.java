package ru.job4j.crud.logic;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.crud.model.Credentials;
import ru.job4j.crud.model.Info;
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

    private final User userOneRoleAdmin = new User(new Credentials("login_1", "password_1", ADMIN), new Info("name_1", "e@mail.com_1", "country_1", "city_1"));
    private final User userTwoRoleUser = new User(new Credentials("login_2", "password_2", USER), new Info("name_2", "e@mail.com_2", "country_2", "city_2"));
    private final User userThreeRoleUser = new User(new Credentials("login_3", "password_3", USER), new Info("name_3", "e@mail.com_3", "country_3", "city_3"));

    private final User userLoginNull = new User(new Credentials(null, "password", ADMIN), new Info("name", "e@mail.com", "country", "city"));
    private final User userPasswordNull = new User(new Credentials("login", null, ADMIN), new Info("name", "e@mail.com", "country", "city"));
    private final User userRoleNull = new User(new Credentials("login", "password", null), new Info("name", "e@mail.com", "country", "city"));

    private final User userNameNull = new User(new Credentials("login", "password", ADMIN), new Info(null, "e@mail.com", "country", "city"));
    private final User userEmailNull = new User(new Credentials("login", "password", ADMIN), new Info("name", null, "country", "city"));
    private final User userEmailWrongFormat = new User(new Credentials("login", "password", ADMIN), new Info("name", "email", "country", "city"));

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
        int id = validator.add(this.userOneRoleAdmin);
        assertThat(validator.findById(id), is(this.userOneRoleAdmin));
        assertThat(validator.findAll().get(0), is(this.userOneRoleAdmin));
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
        int id = this.validator.add(this.userOneRoleAdmin);
        boolean updateResult = this.validator.update(id, this.userTwoRoleUser);
        User expected = new User(
                this.userTwoRoleUser.getCredentials(),
                this.userTwoRoleUser.getInfo()
        );
        assertThat(updateResult, is(true));
        assertThat(validator.findById(id), is(expected));
    }

    @Test
    public void whenUpdateUserWithWrongIdThenUpdateFalseAndUserNotChanging() {
        int id = this.validator.add(this.userOneRoleAdmin);
        int wrongId = id + 2134;
        boolean updateResult = validator.update(wrongId, this.userTwoRoleUser);
        User expected = new User(
                this.userOneRoleAdmin.getCredentials(),
                this.userOneRoleAdmin.getInfo()
        );
        assertThat(updateResult, is(false));
        assertThat(validator.findById(id), is(expected));
    }

    /**
     * Test update() validation.
     */
    @Test
    public void whenUpdateUserHasWrongEmailThenFalseAndNothingChanges() {
        int id = this.validator.add(this.userOneRoleAdmin);
        boolean updateResult = this.validator.update(id, this.userEmailWrongFormat);
        User expected = new User(
                this.userOneRoleAdmin.getCredentials(),
                this.userOneRoleAdmin.getInfo()
        );
        assertThat(updateResult, is(false));
        assertThat(this.validator.findById(id), is(expected));
    }

    /**
     * Test update() with null fields in updating user.
     */
    @Test
    public void whenUpdateUserWithOnlyLoginNotNullThenLoginChange() {
        int id = this.validator.add(this.userOneRoleAdmin);
        User upd = new User(new Credentials("upd_login", null, null), new Info(null, null, null, null));
        boolean updateResult = this.validator.update(id, upd);
        User expected = new User(
                new Credentials(
                        upd.getCredentials().getLogin(),
                        this.userOneRoleAdmin.getCredentials().getPassword(),
                        this.userOneRoleAdmin.getCredentials().getRole()
                ),
                this.userOneRoleAdmin.getInfo()
        );
        assertThat(updateResult, is(true));
        assertThat(this.validator.findById(id), is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyPasswordNotNullThenPasswordChange() {
        int id = this.validator.add(this.userOneRoleAdmin);
        User upd = new User(new Credentials(null, "upd_password", null), new Info(null, null, null, null));
        boolean updateResult = this.validator.update(id, upd);
        User expected = new User(
                new Credentials(
                        this.userOneRoleAdmin.getCredentials().getLogin(),
                        upd.getCredentials().getPassword(),
                        this.userOneRoleAdmin.getCredentials().getRole()
                ),
                this.userOneRoleAdmin.getInfo()
        );
        assertThat(updateResult, is(true));
        assertThat(this.validator.findById(id), is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyRoleNotNullThenRoleChange() {
        int id = this.validator.add(this.userOneRoleAdmin);
        User upd = new User(new Credentials(null, null, ADMIN), new Info(null, null, null, null));
        boolean updateResult = this.validator.update(id, upd);
        User expected = new User(
                new Credentials(
                        this.userOneRoleAdmin.getCredentials().getLogin(),
                        this.userOneRoleAdmin.getCredentials().getPassword(),
                        upd.getCredentials().getRole()
                ),
                this.userOneRoleAdmin.getInfo()
        );
        assertThat(updateResult, is(true));
        assertThat(this.validator.findById(id), is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyNameNotNullThenNameChange() {
        int id = this.validator.add(this.userOneRoleAdmin);
        User upd = new User(new Credentials(null, null, null), new Info("upd_name_", null, null, null));
        boolean updateResult = this.validator.update(id, upd);
        User expected = new User(
                this.userOneRoleAdmin.getCredentials(),
                new Info(
                        upd.getInfo().getName(),
                        this.userOneRoleAdmin.getInfo().getEmail(),
                        this.userOneRoleAdmin.getInfo().getCountry(),
                        this.userOneRoleAdmin.getInfo().getCity()
                )
        );
        assertThat(updateResult, is(true));
        assertThat(this.validator.findById(id), is(expected));
    }

    @Test
    public void whenUpdateUserWithOnlyEmailNotNullThenEmailChange() {
        int id = this.validator.add(this.userOneRoleAdmin);
        User upd = new User(new Credentials(null, null, null), new Info(null, "upd_email@mail.com", null, null));
        boolean updateResult = this.validator.update(id, upd);
        User expected = new User(
                this.userOneRoleAdmin.getCredentials(),
                new Info(
                        this.userOneRoleAdmin.getInfo().getName(),
                        upd.getInfo().getEmail(),
                        this.userOneRoleAdmin.getInfo().getCountry(),
                        this.userOneRoleAdmin.getInfo().getCity()
                )
        );
        assertThat(updateResult, is(true));
        assertThat(this.validator.findById(id), is(expected));
    }

    @Test
    public void whenUpdateUserWithAllFieldsNullThenNothingChanged() {
        int id = this.validator.add(this.userOneRoleAdmin);
        User upd = new User(new Credentials(null, null, null), new Info(null, null, null, null));
        boolean updateResult = this.validator.update(id, upd);
        User expected = new User(
                this.userOneRoleAdmin.getCredentials(),
                this.userOneRoleAdmin.getInfo()
        );
        assertThat(updateResult, is(true));
        assertThat(this.validator.findById(id), is(expected));
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteUserThenHeIsReturnedAndNotFoundInStore() {
        int id = this.validator.add(this.userOneRoleAdmin);
        assertThat(this.validator.delete(id), is(this.userOneRoleAdmin));
        assertThat(this.validator.findById(id), nullValue());
        assertThat(this.validator.findAll(), is(Collections.EMPTY_LIST));
    }

    @Test
    public void whenDeleteUserWithWrongIdThenReturnedNullAndUserStaysInStorage() {
        int id = this.validator.add(this.userOneRoleAdmin);
        int wrongId = id + 234;
        assertThat(this.validator.delete(wrongId), nullValue());
        assertThat(this.validator.findById(id), is(this.userOneRoleAdmin));
        assertThat(this.validator.findAll(), is(Collections.singletonList(this.userOneRoleAdmin)));
    }

    /**
     * Test findById()
     */
    @Test
    public void whenAddedUsersCanFindThemById() {
        int idOne = this.validator.add(this.userOneRoleAdmin);
        int idTwo = this.validator.add(this.userTwoRoleUser);
        int idThree = this.validator.add(this.userThreeRoleUser);
        assertThat(validator.findById(idTwo), is(this.userTwoRoleUser));
        assertThat(validator.findById(idThree), is(this.userThreeRoleUser));
        assertThat(validator.findById(idOne), is(this.userOneRoleAdmin));
    }

    /**
     * Test findAll()
     */
    @Test
    public void whenAddedUsersThenFindAllReturnsThemAll() {
        this.validator.add(this.userOneRoleAdmin);
        this.validator.add(this.userTwoRoleUser);
        this.validator.add(this.userThreeRoleUser);
        User[] result = validator.findAll().toArray(new User[0]);
        User[] expected = {this.userTwoRoleUser, this.userOneRoleAdmin, this.userThreeRoleUser};    // order shouldn't matter in assert
        assertThat(result, arrayContainingInAnyOrder(expected));
    }

}