package ru.job4j.crud.model;

import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.crud.model.Role.ADMIN;
import static ru.job4j.crud.model.Role.USER;

public class UserTest {

    private final User givenId = new User(32, 123L, new Credentials("login", "password", ADMIN), new Info("name", "e@mail.com", "country", "city"));
    private final User defaultId = new User(new Credentials("login", "password", ADMIN), new Info("name", "e@mail.com", "country", "city"));

    /**
     * Test constructors and getters.
     */
    @Test
    public void whenGetterThenValue() {
        // with given id
        assertThat(this.givenId.getId(), is(32));
        assertThat(this.givenId.getCreated(), is(123L));
        assertThat(this.givenId.getCredentials(), is(new Credentials("login", "password", ADMIN)));
        assertThat(this.givenId.getInfo(), is(new Info("name", "e@mail.com", "country", "city")));
        // with default id and create date
        assertThat(this.defaultId.getId(), is(-1));
        assertThat(this.defaultId.getCredentials(), is(new Credentials("login", "password", ADMIN)));
        assertThat(this.defaultId.getInfo(), is(new Info("name", "e@mail.com", "country", "city")));
    }

    /**
     * Test toString()
     */
    @Test
    public void whenToStringThenStringAsNeeded() {
        String expected = String.format(
                "[user id=%s, created=%s, %s, %s]",
                32,
                Instant.ofEpochMilli(123L).atZone(ZoneId.systemDefault()),
                new Credentials("login", "password", ADMIN),
                new Info("name", "e@mail.com", "country", "city")
        );
        assertThat(this.givenId.toString(), is(expected));
    }

    /**
     * Test equals() and hashCode()
     */
    @Test
    public void testEqualsVariantsAndHashcode() {
        User main = new User(32, 123L, new Credentials("login", "password", USER), new Info("name", "e@mail.com", "country", "city"));
        // Vacancies to compare
        User itself = main;
        User same = new User(32, 123L, new Credentials("login", "password", USER), new Info("name", "e@mail.com", "country", "city"));
        User idOther = new User(64, 123L, new Credentials("login", "password", USER), new Info("name", "e@mail.com", "country", "city"));
        User createdOther = new User(32, 353L, new Credentials("login", "password", USER), new Info("name", "e@mail.com", "country", "city"));
        User credentialsOther = new User(32, 123L, new Credentials("other_login", "other_password", USER), new Info("name", "e@mail.com", "country", "city"));
        User infoOther = new User(32, 123L, new Credentials("login", "password", USER), new Info("other_name", "other_e@mail.com", "other_country", "other_city"));
        String classOther = "I'm the User!";
        User nullUser = null;
        // equal
        assertThat(main.equals(itself), is(true));
        assertThat(main.equals(same), is(true));
        assertThat(main.equals(idOther), is(true));
        assertThat(main.equals(createdOther), is(true));
        // hashcode of equal
        assertThat(main.hashCode() == itself.hashCode(), is(true));
        assertThat(main.hashCode() == same.hashCode(), is(true));
        assertThat(main.hashCode() == idOther.hashCode(), is(true));
        assertThat(main.hashCode() == createdOther.hashCode(), is(true));
        // not equal
        assertThat(main.equals(credentialsOther), is(false));
        assertThat(main.equals(infoOther), is(false));
        assertThat(main.equals(classOther), is(false));
        assertThat(main.equals(nullUser), is(false));
    }

    /**
     * Test changeId()
     */
    @Test
    public void whenChangeIdThenUsersEqualButOtherId() {
        User changed = this.givenId.changeId(512);
        assertThat(changed, is(this.givenId));
        assertThat(changed.getId(), is(512));
    }

}