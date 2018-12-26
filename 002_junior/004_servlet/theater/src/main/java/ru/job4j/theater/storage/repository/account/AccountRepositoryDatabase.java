package ru.job4j.theater.storage.repository.account;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.storage.ObjectForm;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.theater.storage.database.DatabaseApi;
import ru.job4j.util.database.DbExecutor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Account repository based on database.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class AccountRepositoryDatabase implements AccountRepository {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(AccountRepositoryDatabase.class);
    /**
     * Singleton instance.
     */
    private static final AccountRepository INSTANCE = new AccountRepositoryDatabase();
    /**
     * Database API instance.
     */
    private final DatabaseApi database = Database.getInstance();

    /**
     * Constructor.
     */
    private AccountRepositoryDatabase() {
    }

    /**
     * Returns INSTANCE.
     *
     * @return Value of INSTANCE field.
     */
    public static AccountRepository getInstance() {
        return INSTANCE;
    }

    /**
     * Add object to repository.
     *
     * @param account Object to add.
     */
    @Override
    public void add(Account account) {
        try (DbExecutor executor = this.database.getExecutor()) {
            this.add(account, executor);
            executor.commit();
        }
    }

    /**
     * Add object to repository.
     *
     * @param account Object to add.
     */
    @Override
    public void add(Account account, DbExecutor executor) {
        executor.execute(
                this.database.getQuery("sql.query.account.add"),
                List.of(account.getName(), account.getPhone()),
                PreparedStatement::execute);
    }

    /**
     * Get list of all accounts in the repository.
     *
     * @return List of accounts.
     */
    @Override
    public List<Account> getAll() {
        try (DbExecutor executor = this.database.getExecutor()) {
            return this.getAll(executor);
        }
    }

    /**
     * Get list of all accounts in the repository.
     *
     * @return List of accounts.
     */
    @Override
    public List<Account> getAll(DbExecutor executor) {
        var values = executor.executeQuery(
                this.database.getQuery("sql.query.account.get_all"),
                List.of(String.class, String.class)
        ).orElse(new ArrayList<>());
        return values.stream()
                .map(ObjectForm::formAccount)
                .collect(Collectors.toList());
    }

    /**
     * Returns account which has given name and phone.
     *
     * @param name  Needed name.
     * @param phone Needed phone.
     * @return Account with needed name and phone or empty Account object.
     */
    @Override
    public Account getByNamePhone(String name, String phone) {
        try (DbExecutor executor = this.database.getExecutor()) {
            return this.getByNamePhone(name, phone, executor);
        }
    }

    /**
     * Returns account which has given name and phone.
     *
     * @param name  Needed name.
     * @param phone Needed phone.
     * @return Account with needed name and phone or empty Account object.
     */
    @Override
    public Account getByNamePhone(String name, String phone, DbExecutor executor) {
        var values = executor.executeQuery(
                this.database.getQuery("sql.query.account.get_by_name_phone"),
                List.of(name, phone),
                List.of(String.class, String.class)
        ).orElse(new ArrayList<>());
        return values.size() > 0
                ? ObjectForm.formAccount(values.get(0))
                : Account.getEmptyAccount();
    }


    /**
     * Clears the repository, removing all objects stored inside.
     */
    @Override
    public void clear() throws SQLException {
        this.database.clearTables();
    }
}
