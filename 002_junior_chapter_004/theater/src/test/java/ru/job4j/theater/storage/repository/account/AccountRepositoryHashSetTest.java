package ru.job4j.theater.storage.repository.account;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.theater.model.Account;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AccountRepositoryHashSetTest {

    private final AccountRepository repository = AccountRepositoryHashSet.getInstance();

    private final Account accountOne = new Account.Builder("acc_1", "123-456").build();
    private final Account accountTwo = new Account.Builder("acc_2", "678-910").build();
    private final Account accountThree = new Account.Builder("acc_3", "876-5543").build();

    @Before
    public void clearRepository() throws SQLException {
        this.repository.clear();
    }

    /**
     * Test add() and getAll()
     */
    @Test
    public void whenAddSeatsThenGetAllReturnsThem() throws SQLException {
        this.repository.add(this.accountOne);
        this.repository.add(this.accountTwo);
        this.repository.add(this.accountThree);
        List<Account> result = this.repository.getAll();
        result.sort(Comparator.comparing(Account::getName).thenComparing(Account::getPhone));
        List<Account> expected = Arrays.asList(this.accountOne, this.accountTwo, this.accountThree);
        assertThat(result, is(expected));
    }

    /**
     * Test getByPlace()
     */
    @Test
    public void whenGivenRowAndColumnFindSeat() throws SQLException {
        this.repository.add(this.accountOne);
        assertThat(this.repository
                        .getByNamePhone(this.accountOne.getName(), this.accountOne.getPhone()),
                is(this.accountOne));
    }

}