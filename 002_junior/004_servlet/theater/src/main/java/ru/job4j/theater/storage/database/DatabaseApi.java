package ru.job4j.theater.storage.database;

import ru.job4j.util.database.DbExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DatabaseApi {
    /**
     * Clear all data from the database tables.
     * Doesn't drop existing tables and functions.
     */
    void clearTables() throws SQLException;

    /**
     * Drop all existing tables and functions,
     * create all tables and functions described for this database.
     */
    void dropAndRecreateStructure() throws SQLException;

    /**
     * Returns executor of sql queries.
     *
     * @return Database executor.
     */
    DbExecutor getExecutor();

    /**
     * Get SQL query by key.
     *
     * @param key Key to the query.
     * @return SQL query.
     */
    String getQuery(String key);

    /**
     * Executes PreparedStatements as one transaction.
     *
     * @param connection Connection to use.
     * @param statements Statements to execute.
     * @throws SQLException If some problems with database happen.
     */
    void executeTransaction(Connection connection, PreparedStatement... statements) throws SQLException;

    Connection getConnection();
}
