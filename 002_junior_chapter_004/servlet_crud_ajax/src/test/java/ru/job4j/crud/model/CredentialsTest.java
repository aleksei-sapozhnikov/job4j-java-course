package ru.job4j.crud.model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.crud.model.Credentials.Role.ADMIN;
import static ru.job4j.crud.model.Credentials.Role.USER;

public class CredentialsTest {
    /**
     * Credentials to test.
     */
    private final Credentials credentialsAdmin = new Credentials("admin_login", "admin_password", ADMIN);
    private final Credentials credentialsUser = new Credentials("user_login", "user_password", USER);
    /**
     * Credentials with null fields.
     */
    private final Credentials credentialsLoginNull = new Credentials(null, "new_password", ADMIN);
    private final Credentials credentialsPasswordNull = new Credentials("new_login", null, ADMIN);
    private final Credentials credentialsRoleNull = new Credentials("new_login", "new_password", null);

    /**
     * Test getters.
     */
    @Test
    public void whenGetterThenValue() {
        // admin
        assertThat(this.credentialsAdmin.getLogin(), is("admin_login"));
        assertThat(this.credentialsAdmin.getPassword(), is("admin_password"));
        assertThat(this.credentialsAdmin.getRole(), is(ADMIN));
        // user
        assertThat(this.credentialsUser.getLogin(), is("user_login"));
        assertThat(this.credentialsUser.getPassword(), is("user_password"));
        assertThat(this.credentialsUser.getRole(), is(USER));
    }

    /**
     * Test toString()
     */
    @Test
    public void whenToStringThenStringAsNeeded() {
        String expected = String.format(
                "[credentials login=%s, password=%s, role=%s]",
                this.credentialsAdmin.getLogin(),
                this.credentialsAdmin.getPassword(),
                this.credentialsAdmin.getRole()
        );
        assertThat(this.credentialsAdmin.toString(), is(expected));
    }

    /**
     * Test equals() and hashCode()
     */
    @Test
    public void testEqualsVariantsAndHashcode() {
        Credentials main = this.credentialsAdmin;
        // objects to compare
        Credentials itself = this.credentialsAdmin;
        Credentials same = new Credentials("admin_login", "admin_password", ADMIN);
        Credentials loginOther = new Credentials("other_login", "admin_password", ADMIN);
        Credentials passwordOther = new Credentials("admin_login", "other_password", ADMIN);
        Credentials roleOther = new Credentials("admin_login", "admin_password", USER);
        Credentials nullObject = null;
        String classOther = "I'm Credentials!";
        // equal
        assertThat(main.equals(itself), is(true));
        assertThat(main.equals(same), is(true));
        // hashcode of equal objects
        assertThat(main.hashCode() == itself.hashCode(), is(true));
        assertThat(main.hashCode() == same.hashCode(), is(true));
        // not equal
        assertThat(main.equals(loginOther), is(false));
        assertThat(main.equals(passwordOther), is(false));
        assertThat(main.equals(nullObject), is(false));
        assertThat(main.equals(classOther), is(false));
    }

    /**
     * Test mergeWith()
     */
    @Test
    public void whenMergeWithNewerThenFieldsUpdate() {
        Credentials result = this.credentialsAdmin.mergeWith(this.credentialsUser);
        assertThat(result.getLogin(), is(this.credentialsUser.getLogin()));
        assertThat(result.getPassword(), is(this.credentialsUser.getPassword()));
        assertThat(result.getRole(), is(this.credentialsUser.getRole()));
    }

    @Test
    public void whenNewerHasNullValueThenMergeRemainsOldValue() {
        // new login null
        Credentials resultLogin = this.credentialsUser.mergeWith(this.credentialsLoginNull);
        assertThat(resultLogin.getLogin(), is(this.credentialsUser.getLogin()));
        assertThat(resultLogin.getPassword(), is(this.credentialsLoginNull.getPassword()));
        assertThat(resultLogin.getRole(), is(this.credentialsLoginNull.getRole()));
        // new password null
        Credentials resultPassword = this.credentialsUser.mergeWith(this.credentialsPasswordNull);
        assertThat(resultPassword.getLogin(), is(this.credentialsPasswordNull.getLogin()));
        assertThat(resultPassword.getPassword(), is(this.credentialsUser.getPassword()));
        assertThat(resultPassword.getRole(), is(this.credentialsPasswordNull.getRole()));
        // new role null
        Credentials resultRole = this.credentialsUser.mergeWith(this.credentialsRoleNull);
        assertThat(resultRole.getLogin(), is(this.credentialsRoleNull.getLogin()));
        assertThat(resultRole.getPassword(), is(this.credentialsRoleNull.getPassword()));
        assertThat(resultRole.getRole(), is(this.credentialsUser.getRole()));
    }

}