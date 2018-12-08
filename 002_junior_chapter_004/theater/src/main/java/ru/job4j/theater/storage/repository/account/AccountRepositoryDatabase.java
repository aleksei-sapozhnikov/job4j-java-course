package ru.job4j.theater.storage.repository.account;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.storage.DatabaseObjectUtils;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.theater.storage.database.DatabaseApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public void add(Account account) throws SQLException {
        try (Connection connection = this.database.getConnection();
             PreparedStatement statement = connection.prepareStatement(this.database.getQuery("sql.query.account.add"))
        ) {
            DatabaseObjectUtils.FillStatement.AccountStatements.fillAdd(statement, account);
            statement.execute();
        }
    }


    /**
     * Get list of all accounts in the repository.
     *
     * @return List of accounts.
     */
    @Override
    public List<Account> getAll() throws SQLException {
        List<Account> list = new ArrayList<>();
        try (Connection connection = this.database.getConnection();
             PreparedStatement statement = connection.prepareStatement(this.database.getQuery("sql.query.account.get_all"));
             ResultSet res = statement.executeQuery()
        ) {
            while (res.next()) {
                list.add(DatabaseObjectUtils.FormObject.formAccount(res));
            }
        }
        return list;
    }

    /**
     * Returns account which has given name and phone.
     *
     * @param name  Needed name.
     * @param phone Needed phone.
     * @return Account with needed name and phone or empty Account object.
     */
    @Override
    public Account getByNamePhone(String name, String phone) throws SQLException {
        Account result = Account.getEmptyAccount();
        try (Connection connection = this.database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     this.database.getQuery("sql.query.account.get_by_name_phone"))
        ) {
            DatabaseObjectUtils.FillStatement.AccountStatements.fillGetByNamePhone(statement, name, phone);
            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    result = DatabaseObjectUtils.FormObject.formAccount(res);
                }
            }
        }
        return result;
    }


    /**
     * Clears the repository, removing all objects stored inside.
     */
    @Override
    public void clear() throws SQLException {
        this.database.clearTables();
    }
}
