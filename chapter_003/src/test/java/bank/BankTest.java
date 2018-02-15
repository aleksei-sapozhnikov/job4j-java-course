package bank;

import bank.exceptions.AlreadyExistsException;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the User class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
public class BankTest {

    /**
     * Test addUser() method.
     */
    @Test
    public void whenAddUserThenNewUserInMap() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addUser(new User("323-43", "Petya"));
        Set<User> result = bank.showAllUsers();
        Set<User> expected = new HashSet<>(Arrays.asList(new User("123-45", "Vasya"), new User("323-43", "Petya")));
        assertThat(result, is(expected));
    }

    @Test(expected = AlreadyExistsException.class)
    public void whenAddUserAndUserExistsThenAlreadyExistsException() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "John"));
        bank.addUser(new User("123-45", "John"));
    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void addAccountToUser() {
    }

    @Test
    public void deleteAccountFromUser() {
    }

    @Test
    public void getUserAndAccounts() {
    }

    @Test
    public void getAccountByRequisites() {
    }

    @Test
    public void replace() {
    }

    @Test
    public void transferMoney() {
    }
}