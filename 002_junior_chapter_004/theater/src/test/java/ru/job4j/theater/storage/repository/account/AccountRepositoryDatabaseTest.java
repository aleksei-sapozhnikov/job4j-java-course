package ru.job4j.theater.storage.repository.account;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.util.methods.CommonUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AccountRepositoryDatabaseTest {

    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(AccountRepositoryDatabaseTest.class);

    static {
        try {
            Database.getInstance().dropAndRecreateStructure();
        } catch (SQLException e) {
            LOG.error(CommonUtils.describeThrowable(e));
        }
    }

    private final AccountRepository repository = AccountRepositoryDatabase.getInstance();

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
    public void whenAddAccountsThenGetAllReturnsThem() {
        this.repository.add(this.accountOne);
        this.repository.add(this.accountTwo);
        this.repository.add(this.accountThree);
        List<Account> result = this.repository.getAll();
        result.sort(Comparator.comparing(Account::getName).thenComparing(Account::getPhone));
        List<Account> expected = Arrays.asList(this.accountOne, this.accountTwo, this.accountThree);
        assertThat(result, is(expected));
    }

    @Test
    public void whenGivenNamePhoneReturnsFoundAccountOrEmptyAccount() {
        this.repository.add(this.accountOne);
        String name = this.accountOne.getName();
        String phone = this.accountOne.getPhone();
        assertThat(this.repository.getByNamePhone(name, phone), is(this.accountOne));
        assertThat(this.repository.getByNamePhone(name, "no such phone"), is(Account.getEmptyAccount()));
        assertThat(this.repository.getByNamePhone("no such name", phone), is(Account.getEmptyAccount()));
        assertThat(this.repository.getByNamePhone("no such name", "no such phone"), is(Account.getEmptyAccount()));
    }
}