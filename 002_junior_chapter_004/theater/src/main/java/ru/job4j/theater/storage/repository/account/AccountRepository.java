package ru.job4j.theater.storage.repository.account;

import ru.job4j.theater.model.Account;

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
    void add(Account account) throws SQLException;

    /**
     * Get list of all accounts in the repository.
     *
     * @return List of accounts.
     */
    List<Account> getAll() throws SQLException;

    /**
     * Returns acc which has given place and column.
     *
     * @param name  Account name.
     * @param phone Account phone.
     * @return Account with needed name and phone or empty Account object.
     */
    Account getByNamePhone(String name, String phone) throws SQLException;

    /**
     * Clears the repository, removing all objects stored inside.
     */
    void clear() throws SQLException;
}
