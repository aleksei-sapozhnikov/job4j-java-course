package ru.job4j.crud.model;

import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.crud.model.Role.ADMIN;
import static ru.job4j.crud.model.Role.USER;

public class UserTest {

    private final User givenId = new User(32, "name", "login", "password", "e@mail.com", 123L, ADMIN, "country", "city");
    private final User defaultId = new User("name", "login", "password", "e@mail.com", 123L, USER, "country", "city");

    /**
     * Test constructors and getters.
     */
    @Test
    public void whenGetterThenValue() {
        // with given id
        assertThat(this.givenId.getId(), is(32));
        assertThat(this.givenId.getName(), is("name"));
        assertThat(this.givenId.getLogin(), is("login"));
        assertThat(this.givenId.getEmail(), is("e@mail.com"));
        assertThat(this.givenId.getCreated(), is(123L));
        // with default id
        assertThat(this.defaultId.getId(), is(-1));
        assertThat(this.defaultId.getName(), is("name"));
        assertThat(this.defaultId.getLogin(), is("login"));
        assertThat(this.defaultId.getEmail(), is("e@mail.com"));
        assertThat(this.defaultId.getCreated(), is(123L));
    }

    /**
     * Test toString()
     */
    @Test
    public void whenToStringThenStringAsNeeded() {
        String expected = String.format(
                "[user id=%s, name=%s, login=%s, password=%s, email=%s, created=%s, country=%s, city=%s]",
                this.givenId.getId(),
                this.givenId.getName(),
                this.givenId.getLogin(),
                this.givenId.getPassword(),
                this.givenId.getEmail(),
                Instant.ofEpochMilli(this.givenId.getCreated()).atZone(ZoneId.systemDefault()),
                this.givenId.getCountry(),
                this.givenId.getCity()
        );
        assertThat(this.givenId.toString(), is(expected));
    }

    /**
     * Test equals() and hashCode()
     */
    @Test
    public void testEqualsVariantsAndHashcode() {
        User main = new User(32, "name", "login", "password", "e@mail.com", 123L, ADMIN, "country", "city");
        // Vacancies to compare
        User itself = main;
        User same = new User(32, "name", "login", "password", "e@mail.com", 123L, ADMIN, "country", "city");
        User idOther = new User(43, "name", "login", "password", "e@mail.com", 123L, ADMIN, "country", "city");
        User nameOther = new User(32, "otherName", "login", "password", "e@mail.com", 123L, ADMIN, "country", "city");
        User loginOther = new User(32, "name", "otherLogin", "password", "e@mail.com", 123L, ADMIN, "country", "city");
        User passwordOther = new User(32, "name", "otherLogin", "otherPassword", "e@mail.com", 123L, ADMIN, "country", "city");
        User emailOther = new User(32, "name", "login", "password", "eOther@mail.com", 123L, ADMIN, "country", "city");
        User createdOther = new User(32, "name", "login", "password", "e@mail.com", 456L, ADMIN, "country", "city");
        User roleOther = new User(32, "name", "otherLogin", "password", "e@mail.com", 123L, USER, "country", "city");
        User countryOther = new User(32, "name", "otherLogin", "password", "e@mail.com", 123L, USER, "otherCountry", "city");
        User cityOther = new User(32, "name", "otherLogin", "password", "e@mail.com", 123L, USER, "country", "otherCity");
        String classOther = "I'm the User!";
        User nullUser = null;
        // equal
        assertThat(main.equals(itself), is(true));
        assertThat(main.equals(same), is(true));
        assertThat(main.equals(idOther), is(true));
        // not equal
        assertThat(main.equals(nameOther), is(false));
        assertThat(main.equals(loginOther), is(false));
        assertThat(main.equals(passwordOther), is(false));
        assertThat(main.equals(emailOther), is(false));
        assertThat(main.equals(createdOther), is(false));
        assertThat(main.equals(roleOther), is(false));
        assertThat(main.equals(countryOther), is(false));
        assertThat(main.equals(cityOther), is(false));
        assertThat(main.equals(classOther), is(false));
        assertThat(main.equals(nullUser), is(false));
        // hashcode of equal
        assertThat(main.hashCode() == itself.hashCode(), is(true));
        assertThat(main.hashCode() == same.hashCode(), is(true));
        assertThat(main.hashCode() == idOther.hashCode(), is(true));
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