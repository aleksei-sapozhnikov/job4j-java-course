package ru.job4j.createbeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.createbeans.util.Connector;
import ru.job4j.createbeans.util.FunctionEx;
import ru.job4j.createbeans.util.User;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Database user storage.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class StorageJdbc implements Storage {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(StorageJdbc.class);

    /**
     * Connection object to perform operations on.
     */
    private final Connection connection;

    /**
     * Acquires connection.
     *
     * @param connector Supplies connection object(s).
     */
    public StorageJdbc(Connector connector) {
        this.connection = connector.getConnection();
    }

    /**
     * Adds user to database.
     *
     * @param user User to add.
     * @return Id given by storage.
     */
    @Override
    public long add(User user) {
        if (user.getId() != 0) {
            throw new RuntimeException("Adding user with certain id is prohibited");
        }
        return this.processStatement("insert into users (name) values (?) returning id", statement -> {
            long id;
            statement.setString(1, user.getName());
            try (var res = statement.executeQuery()) {
                res.next();
                id = res.getLong("id");
                user.setId(id);
            }
            return id;
        });
    }

    /**
     * Returns user by id.
     *
     * @param id Id in storage.
     * @return User ogject.
     */
    @Override
    public User get(long id) {
        return this.processStatement("select * from users where id = ?", statement -> {
            User result = null;
            statement.setLong(1, id);
            try (var res = statement.executeQuery()) {
                if (res.next()) {
                    result = new User(res.getString("name"));
                    result.setId(res.getLong("id"));
                }
            }
            return result;
        });
    }

    /**
     * Removes user from storage.
     *
     * @param id Id in storage.
     */
    @Override
    public void remove(long id) {
        this.processStatement("delete from users where id = ?", statement -> {
            statement.setLong(1, id);
            statement.executeUpdate();
            return null;
        });
    }

    /**
     * Common operation for statement processing.
     *
     * @param query      Query to process.
     * @param operations Operations to perform on query.
     * @param <T>        Generic result type.
     * @return Result of query processing.
     */
    private <T> T processStatement(String query, FunctionEx<PreparedStatement, T> operations) {
        T result = null;
        try (var statement = this.connection.prepareStatement(query)) {
            result = operations.apply(statement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
