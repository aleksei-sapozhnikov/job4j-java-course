package bank;

import bank.exceptions.AlreadyExistsException;
import bank.exceptions.NotFoundException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

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

    /**
     * Test deleteUser() method.
     */
    @Test
    public void whenDeleteUserOfTwoThenOneLeft() throws AlreadyExistsException, NotFoundException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addUser(new User("323-43", "Petya"));
        bank.deleteUser(new User("323-43", "Petya"));
        Set<User> result = bank.showAllUsers();
        Set<User> expected = new HashSet<>(Arrays.asList(new User("123-45", "Vasya")));
        assertThat(result, is(expected));
    }

    @Test
    public void whenDeleteLastUserThenEmptyUserList() throws AlreadyExistsException, NotFoundException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.deleteUser(new User("123-45", "Vasya"));
        Set<User> result = bank.showAllUsers();
        Set<User> expected = new HashSet<>();
        assertThat(result, is(expected));
    }

    @Test(expected = NotFoundException.class)
    public void whenDeleteNonExistentUserThenNotFoundException() throws AlreadyExistsException, NotFoundException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addUser(new User("323-43", "Petya"));
        bank.deleteUser(new User("759-43", "Anna"));
    }

    /**
     * Test addAccountToUser() method.
     */
    @Test
    public void whenAddAccountThenItIsInUserAccountList() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        List<Account> result = bank.getUserAccounts("123-45");
        List<Account> expected = new ArrayList<>(Arrays.asList(new Account("N-82", new BigDecimal("123.45"))));
        assertThat(result, is(expected));
    }

    @Test
    public void whenAddThreeAccountsTheyAreInUserAccountList() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        bank.addAccountToUser("123-45", new Account("N-54", new BigDecimal("443.45")));
        bank.addAccountToUser("123-45", new Account("N-23", new BigDecimal("1545")));
        List<Account> result = bank.getUserAccounts("123-45");
        List<Account> expected = new ArrayList<>(Arrays.asList(
                new Account("N-82", new BigDecimal("123.45")),
                new Account("N-54", new BigDecimal("443.45")),
                new Account("N-23", new BigDecimal("1545"))
        ));
        assertThat(result, is(expected));
    }

    @Test
    public void deleteAccountFromUser() {
    }

    @Test
    public void getAccountByRequisites() {
    }

    /**
     * Test transferMoney() method.
     */
    @Test
    public void whenTransferMoneyFromUserToAnotherUserThenValueChangesRight() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        bank.addUser(new User("543-21", "Petya"));
        bank.addAccountToUser("543-21", new Account("G-64", new BigDecimal("78.15")));
        bank.transferMoney("123-45", "N-82", "543-21", "G-64", new BigDecimal("12.15"));
        List<Account> resultOne = bank.getUserAccounts("123-45");
        List<Account> resultTwo = bank.getUserAccounts("543-21");
        List<Account> expectedOne = new ArrayList<>(Arrays.asList(new Account("N-82", new BigDecimal("111.30"))));
        List<Account> expectedTwo = new ArrayList<>(Arrays.asList(new Account("G-64", new BigDecimal("90.30"))));
        assertThat(resultOne, is(expectedOne));
        assertThat(resultTwo, is(expectedTwo));
    }

    @Test
    public void whenTransferMoneyFromUserToTheSameUserDifferentAccountThenValueChangesRight() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        bank.addAccountToUser("123-45", new Account("G-64", new BigDecimal("78.15")));
        bank.transferMoney("123-45", "N-82", "123-45", "G-64", new BigDecimal("12.15"));
        List<Account> result = bank.getUserAccounts("123-45");
        List<Account> expected = new ArrayList<>(Arrays.asList(
                new Account("N-82", new BigDecimal("111.30")),
                new Account("G-64", new BigDecimal("90.30"))
        ));
        assertThat(result, is(expected));
    }

    @Test
    public void whenTransferMoneyFromAccountToTheSameAccountThenFalseAndNoAccountChange() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        boolean resultOne = bank.transferMoney("123-45", "N-82", "123-45", "N-82", new BigDecimal("12.15"));
        List<Account> resultTwo = bank.getUserAccounts("123-45");
        List<Account> expectedTwo = new ArrayList<>(Arrays.asList(new Account("N-82", new BigDecimal("123.45"))));
        assertThat(resultOne, is(false));
        assertThat(resultTwo, is(expectedTwo));
    }

}