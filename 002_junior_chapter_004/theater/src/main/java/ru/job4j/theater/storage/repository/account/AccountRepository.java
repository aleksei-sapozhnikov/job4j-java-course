package ru.job4j.theater.storage.repository.account;

import ru.job4j.theater.model.Account;
import ru.job4j.util.database.DbExecutor;

import java.sql.SQLException;
import java.util.List;

/**
 * Repository for Account objects.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface AccountRepository {
    /**
     * Add object to repository.
     *
     * @param account Object to add.
     */
    void add(Account account);

    /**
     * Get list of all accounts in the repository.
     *
     * @return List of accounts.
     */
    List<Account> getAll();

    /**
     * Returns acc which has given place and column.
     *
     * @param name  Account name.
     * @param phone Account phone.
     * @return Account with needed name and phone or empty Account object.
     */
    Account getByNamePhone(String name, String phone);

    /**
     * Clears the repository, removing all objects stored inside.
     */
    void clear() throws SQLException;


    /**
     * Add object to repository.
     * Using given executor, no auto-commit.
     *
     * @param executor Database executor.
     * @param account  Object to add.
     */
    default void add(Account account, DbExecutor executor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Get list of all accounts in the repository.
     * Using given executor, no auto-commit.
     *
     * @param executor Database executor.
     * @return List of accounts.
     */
    default List<Account> getAll(DbExecutor executor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Returns acc which has given place and column.
     * Using given executor, no auto-commit.
     *
     * @param name     Account name.
     * @param phone    Account phone.
     * @param executor Database executor.
     * @return Account with needed name and phone or empty Account object.
     */
    default Account getByNamePhone(String name, String phone, DbExecutor executor) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
