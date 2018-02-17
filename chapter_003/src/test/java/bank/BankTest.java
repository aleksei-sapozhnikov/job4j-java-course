package bank;

import bank.exceptions.AlreadyExistsException;
import bank.exceptions.NotFoundException;
import org.junit.Test;

import java.math.BigDecimal;
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
        Set<Account> result = bank.getUserAccounts("123-45");
        Set<Account> expected = new HashSet<>(Arrays.asList(new Account("N-82", new BigDecimal("123.45"))));
        assertThat(result, is(expected));
    }

    @Test
    public void whenAddThreeAccountsTheyAreInUserAccountList() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        bank.addAccountToUser("123-45", new Account("N-54", new BigDecimal("443.45")));
        bank.addAccountToUser("123-45", new Account("N-23", new BigDecimal("1545")));
        Set<Account> result = bank.getUserAccounts("123-45");
        Set<Account> expected = new HashSet<>(Arrays.asList(
                new Account("N-82", new BigDecimal("123.45")),
                new Account("N-54", new BigDecimal("443.45")),
                new Account("N-23", new BigDecimal("1545"))
        ));
        assertThat(result, is(expected));
    }

    @Test(expected = AlreadyExistsException.class)
    public void whenAbsolutelyTheSameAccountAlreadyExistsThenAlreadyExistsException() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("653.45")));
    }

    @Test
    public void whenDifferentUsersThenCanHaveTheSameAccounts() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addUser(new User("543-21", "Lena"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        bank.addAccountToUser("543-21", new Account("N-82", new BigDecimal("653.45")));
        Set<Account> resultOne = bank.getUserAccounts("123-45");
        Set<Account> resultTwo = bank.getUserAccounts("543-21");
        Set<Account> expectedOne = new HashSet<>(Arrays.asList(new Account("N-82", new BigDecimal("123.45"))));
        Set<Account> expectedTwo = new HashSet<>(Arrays.asList(new Account("N-82", new BigDecimal("653.45"))));
        assertThat(resultOne, is(expectedOne));
        assertThat(resultTwo, is(expectedTwo));
    }

    /**
     * Test deleteAccountFromUser() method.
     */
    @Test
    public void whenDeleteAccountFromTwoThenOneAccountLeft() throws AlreadyExistsException, NotFoundException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        bank.addAccountToUser("123-45", new Account("G-642", new BigDecimal("653.45")));
        bank.deleteAccountFromUser("123-45", new Account("G-642", new BigDecimal("653.45")));
        Set<Account> result = bank.getUserAccounts("123-45");
        Set<Account> expected = new HashSet<>(Arrays.asList(new Account("N-82", new BigDecimal("123.45"))));
        assertThat(result, is(expected));
    }

    @Test(expected = NotFoundException.class)
    public void whenDeleteAccountNotFoundThenNotFoundException() throws AlreadyExistsException, NotFoundException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        bank.deleteAccountFromUser("123-45", new Account("G-642", new BigDecimal("653.45")));
    }

    @Test(expected = NotFoundException.class)
    public void whenDeleteAccountUserNotFoundThenNotFoundException() throws AlreadyExistsException, NotFoundException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        bank.deleteAccountFromUser("222-444", new Account("N-82", new BigDecimal("123.45")));
    }

    /**
     * Test getUserAccounts() method.
     */
    @Test
    public void whenGetUserAccountsThenAccounts() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("G-942", new BigDecimal("123.45")));
        Set<Account> result = bank.getUserAccounts("123-45");
        Set<Account> expected = new HashSet<>(Arrays.asList(new Account("G-942", new BigDecimal("123.45"))));
        assertThat(result, is(expected));
    }

    /**
     * Test getAccountByRequisites() method.
     */
    @Test
    public void whenExistingRequisitesThenAccount() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        Account result = bank.getAccountByRequisites(bank.getUserAccounts("123-45"), "N-82");
        Account expected = new Account("N-82", new BigDecimal("123.45"));
        assertThat(result, is(expected));
    }

    @Test
    public void whenAccountNotFoundByRequisitesThenNull() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        Account result = bank.getAccountByRequisites(bank.getUserAccounts("123-45"), "G-64");
        Account expected = null;
        assertThat(result, is(expected));
    }

    /**
     * Test transferMoney() method.
     */
    @Test
    public void whenTransferMoneyFromAccountToTheSameAccountThenFalse() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        boolean result = bank.transferMoney("123-45", "N-82", "123-45", "N-82", new BigDecimal("12.15"));
        assertThat(result, is(false));
    }

    @Test
    public void whenNotEnoughMoneyToTransferThenFalse() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        bank.addUser(new User("543-21", "Anna"));
        bank.addAccountToUser("543-21", new Account("G-64", new BigDecimal("543.21")));
        boolean result = bank.transferMoney("123-45", "N-82", "543-21", "G-64", new BigDecimal("250.00"));
        assertThat(result, is(false));
    }

    @Test
    public void whenTransferMoneyToAnotherAccountThenTrue() throws AlreadyExistsException {
        Bank bank = new Bank();
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", new Account("N-82", new BigDecimal("123.45")));
        bank.addUser(new User("543-21", "Anna"));
        bank.addAccountToUser("543-21", new Account("G-64", new BigDecimal("543.21")));
        boolean result = bank.transferMoney("123-45", "N-82", "543-21", "G-64", new BigDecimal("100.00"));
        assertThat(result, is(true));
    }

    @Test
    public void whenTransferMoneyToAnotherAccountThenRightAmountOfMoneyInAccounts() throws AlreadyExistsException {
        Bank bank = new Bank();
        Account source = new Account("N-82", new BigDecimal("123.45"));
        Account destin = new Account("G-64", new BigDecimal("543.21"));
        bank.addUser(new User("123-45", "Vasya"));
        bank.addAccountToUser("123-45", source);
        bank.addUser(new User("543-21", "Anna"));
        bank.addAccountToUser("543-21", destin);
        bank.transferMoney("123-45", "N-82", "543-21", "G-64", new BigDecimal("100.00"));
        boolean resultOne = source.hasValue(new BigDecimal("23.45"));
        boolean resultTwo = destin.hasValue(new BigDecimal("643.21"));
        assertThat(resultOne, is(true));
        assertThat(resultTwo, is(true));
    }
}