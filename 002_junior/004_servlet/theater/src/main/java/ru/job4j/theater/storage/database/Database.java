package ru.job4j.theater.storage.database;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.util.database.CertainPropertiesHolder;
import ru.job4j.util.database.Connector;
import ru.job4j.util.database.DbConnector;
import ru.job4j.util.database.DbExecutor;
import ru.job4j.util.methods.CommonUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Function;

/**
 * Database class implementing methods to work with database.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Database implements DatabaseApi, AutoCloseable {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Database.class);
    /**
     * File with database properties.
     */
    private static final String PROPERTIES_FILE = "ru/job4j/theater/database.properties";
    /**
     * Singleton instance.
     */
    private static final Database INSTANCE = new Database();

    /**
     * Connector to database.
     */
    private final Connector connector;
    /**
     * Database queries holder.
     */
    private final Function<String, String> queries;

    /**
     * Constructs Database object and initializes needed sub-classes.
     */
    private Database() {
        Properties properties = CommonUtils.loadProperties(this, PROPERTIES_FILE);
        this.connector = new DbConnector(new BasicDataSource(), properties);
        this.queries = new CertainPropertiesHolder(properties, "sql.");
    }

    /**
     * Returns INSTANCE.
     *
     * @return Value of INSTANCE field.
     */
    public static Database getInstance() {
        return INSTANCE;
    }

    /**
     * Clear all data from the database tables.
     * Doesn't drop existing tables and functions.
     */
    @Override
    public void clearTables() throws SQLException {
        try (Connection connection = this.connector.getConnection();
             PreparedStatement dropTables = connection
                     .prepareStatement(this.queries.apply("sql.structure.tables.clear"))) {
            dropTables.execute();
        }
    }

    /**
     * Drop all existing tables and functions,
     * create all tables and functions described for this database.
     */
    @Override
    public void dropAndRecreateStructure() throws SQLException {
        try (Connection connection = this.connector.getConnection();
             PreparedStatement dropTables = connection.prepareStatement(
                     this.queries.apply("sql.structure.tables.drop"));
             PreparedStatement dropFunctions = connection.prepareStatement(
                     this.queries.apply("sql.structure.functions.drop"));
             PreparedStatement createTables = connection.prepareStatement(
                     this.queries.apply("sql.structure.tables.create"))
        ) {
            this.executeTransaction(connection, dropFunctions, dropTables, createTables);
        }
    }

    /**
     * Executes PreparedStatements as one transaction.
     *
     * @param connection Connection to use.
     * @param statements Statements to execute.
     * @throws SQLException If some problems with database happen.
     */
    public void executeTransaction(Connection connection, PreparedStatement... statements) throws SQLException {
        connection.setAutoCommit(false);
        try {
            for (PreparedStatement statement : statements) {
                statement.execute();
            }
            connection.commit();
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(false);
        }
    }

    /**
     * Returns database Connection object.
     *
     * @return Connection object.
     */
    @Override
    public DbExecutor getExecutor() {
        return new DbExecutor(this.connector.getConnection());
    }

    /**
     * Get SQL query by key.
     *
     * @param key Key to the query.
     * @return SQL query.
     */
    @Override
    public String getQuery(String key) {
        return this.queries.apply(key);
    }

    /**
     * Closes resources.
     *
     * @throws Exception Possible Exception.
     */
    @Override
    public void close() throws Exception {
        this.connector.close();
    }

    @Override
    public Connection getConnection() {
        return this.connector.getConnection();
    }
}