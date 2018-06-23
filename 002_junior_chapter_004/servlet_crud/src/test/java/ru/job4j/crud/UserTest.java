package ru.job4j.crud;

import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserTest {

    /**
     * Test constructors and getters.
     */
    @Test
    public void whenGetterThenValue() {
        // with given id
        User givenId = new User(32, "name", "login", "e@mail.com", 123L);
        assertThat(givenId.getId(), is(32));
        assertThat(givenId.getName(), is("name"));
        assertThat(givenId.getLogin(), is("login"));
        assertThat(givenId.getEmail(), is("e@mail.com"));
        assertThat(givenId.getCreated(), is(123L));
        // with default id = -1
        User defaultId = new User("name", "login", "e@mail.com", 123L);
        assertThat(defaultId.getId(), is(-1));
        assertThat(defaultId.getName(), is("name"));
        assertThat(defaultId.getLogin(), is("login"));
        assertThat(defaultId.getEmail(), is("e@mail.com"));
        assertThat(defaultId.getCreated(), is(123L));
    }

    /**
     * Test toString()
     */
    @Test
    public void whenToStringThenStringAsNeeded() {
        User user = new User(32, "name", "login", "e@mail.com", 123L);
        String result = user.toString();
        String expected = String.format(
                "[user id = %s, name = %s, login = %s, email = %s, created = %s]",
                32, "name", "login", "e@mail.com",
                Instant.ofEpochMilli(123L).atZone(ZoneId.systemDefault()));
        assertThat(result, is(expected));
    }

    /**
     * Test equals() and hashCode()
     */
    @Test
    public void testEqualsVariantsAndHashcode() {
        User main = new User(32, "name", "login", "e@mail.com", 123L);
        // Vacancies to compare
        User itself = main;
        User same = new User(32, "name", "login", "e@mail.com", 123L);
        User idOther = new User(43, "name", "login", "e@mail.com", 123L);
        User nameOther = new User(32, "otherName", "login", "e@mail.com", 123L);
        User loginOther = new User(32, "name", "otherLogin", "e@mail.com", 123L);
        User emailOther = new User(32, "name", "login", "eOther@mail.com", 123L);
        User createdOther = new User(32, "name", "login", "e@mail.com", 456L);
        String classOther = "I'm the User!";
        User nullUser = null;
        // equal
        assertThat(main.equals(itself), is(true));
        assertThat(main.equals(same), is(true));
        assertThat(main.equals(idOther), is(true));
        // not equal
        assertThat(main.equals(nameOther), is(false));
        assertThat(main.equals(loginOther), is(false));
        assertThat(main.equals(emailOther), is(false));
        assertThat(main.equals(createdOther), is(false));
        assertThat(main.equals(classOther), is(false));
        assertThat(main.equals(nullUser), is(false));
        // hashcode of equal
        assertThat(main.hashCode() == itself.hashCode(), is(true));
        assertThat(main.hashCode() == same.hashCode(), is(true));
        assertThat(main.hashCode() == idOther.hashCode(), is(true));
    }

}