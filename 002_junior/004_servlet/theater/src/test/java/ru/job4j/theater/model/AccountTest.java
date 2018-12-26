package ru.job4j.theater.model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class AccountTest {

    @Test
    public void testEmptyEntity() {
        Account expected = new Account.Builder("empty name", "empty phone").build();
        assertThat(Account.getEmptyAccount(), is(expected));
    }

    @Test
    public void testGetters() {
        Account account = new Account.Builder("name one", "123-45").build();
        assertThat(account.getName(), is("name one"));
        assertThat(account.getPhone(), is("123-45"));
    }

    @Test
    public void testToString() {
        Account account = new Account.Builder("name two", "456-789").build();
        assertThat(account.toString(), is("Account[name='name two',phone='456-789']"));
    }

    @Test
    public void testEqualsAndHashCode() {
        Account main = new Account.Builder("name main", "987").build();
        Account same = new Account.Builder("name main", "987").build();
        Account nameOther = new Account.Builder("other", "987").build();
        Account phoneOther = new Account.Builder("name main", "334").build();
        assertThat(main == same, is(false));
        assertThat(main.equals(same), is(true));
        assertThat(main.hashCode() == same.hashCode(), is(true));
        assertThat(main.equals(nameOther), is(false));
        assertThat(main.equals(phoneOther), is(false));
    }
}