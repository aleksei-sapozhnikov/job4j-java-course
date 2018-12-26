package ru.job4j.theater.storage.repository.account;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.theater.storage.database.DatabaseApi;
import ru.job4j.util.database.DbExecutor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AccountRepositoryDatabaseTest {

    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(AccountRepositoryDatabaseTest.class);

    private final DatabaseApi databaseApi = Database.getInstance();
    private final AccountRepository repository = AccountRepositoryDatabase.getInstance();

    private final Account accountOne = new Account.Builder("acc_1", "123-456").build();
    private final Account accountTwo = new Account.Builder("acc_2", "678-910").build();
    private final Account accountThree = new Account.Builder("acc_3", "876-5543").build();

    /**
     * Test add() and getAll()
     */
    @Test
    public void whenAddAccountsThenGetAllReturnsThem() {
        try (DbExecutor executor = this.databaseApi.getExecutor()) {
            this.repository.add(this.accountOne, executor);
            this.repository.add(this.accountTwo, executor);
            this.repository.add(this.accountThree, executor);
            List<Account> result = this.repository.getAll(executor);
            result.sort(Comparator.comparing(Account::getName).thenComparing(Account::getPhone));
            List<Account> expected = Arrays.asList(this.accountOne, this.accountTwo, this.accountThree);
            assertThat(result, is(expected));
        }


    }

    @Test
    public void whenGivenNamePhoneReturnsFoundAccountOrEmptyAccount() {
        try (DbExecutor executor = this.databaseApi.getExecutor()) {
            this.repository.add(this.accountOne, executor);
            String name = this.accountOne.getName();
            String phone = this.accountOne.getPhone();
            assertThat(this.repository.getByNamePhone(name, phone, executor), is(this.accountOne));
            assertThat(this.repository.getByNamePhone(name, "no such phone", executor), is(Account.getEmptyAccount()));
            assertThat(this.repository.getByNamePhone("no such name", phone, executor), is(Account.getEmptyAccount()));
            assertThat(this.repository.getByNamePhone("no such name", "no such phone", executor), is(Account.getEmptyAccount()));
        }

    }
}