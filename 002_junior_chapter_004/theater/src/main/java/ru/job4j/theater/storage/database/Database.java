package ru.job4j.theater.storage.database;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Database class implementing methods to work with database.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Database implements DatabaseApi {
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
    private final DatabaseConnector connector;
    /**
     * Database queries holder.
     */
    private final DatabaseQueries queries;

    /**
     * Constructs Database object and initializes needed sub-classes.
     */
    private Database() {
        Properties properties = Utils.loadProperties(this, PROPERTIES_FILE);
        this.connector = new DatabaseConnector(properties);
        this.queries = new DatabaseQueries(properties);
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
             PreparedStatement dropTables = connection.prepareStatement(this.queries.getQuery("sql.structure.tables.clear"))
        ) {
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
                     this.queries.getQuery("sql.structure.tables.drop"));
             PreparedStatement dropFunctions = connection.prepareStatement(
                     this.queries.getQuery("sql.structure.functions.drop"));
             PreparedStatement createTables = connection.prepareStatement(
                     this.queries.getQuery("sql.structure.tables.create"))
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
    public Connection getConnection() throws SQLException {
        return this.connector.getConnection();
    }

    /**
     * Get SQL query by key.
     *
     * @param key Key to the query.
     * @return SQL query.
     */
    @Override
    public String getQuery(String key) {
        return this.queries.getQuery(key);
    }


    /**
     * Connects to database and returns database connections.
     *
     * @author Aleksei Sapozhnikov (vermucht@gmail.com)
     * @version 0.1
     * @since 0.1
     */
    private static class DatabaseConnector {
        /**
         * Connection pool.
         */
        private final BasicDataSource connectionPool = new BasicDataSource();

        /**
         * Constructs instance using given parameters.
         *
         * @param properties Properties object to take parameters from.
         */
        private DatabaseConnector(Properties properties) {
            this.configureConnectionPool(this.connectionPool, properties);
        }

        /**
         * Configures connection pool.
         *
         * @param pool       ConnectionPool to configure.
         * @param properties Properties object with needed parameters.
         */
        private void configureConnectionPool(BasicDataSource pool, Properties properties) {
            Map<String, String> data = this.formConnectionData(properties);
            this.setParameters(pool, data);
        }

        /**
         * Forms map with data needed to configure connection.
         *
         * @param properties Object with needed data.
         * @return Map with connection data.
         */
        private Map<String, String> formConnectionData(Properties properties) {
            Map<String, String> result = new HashMap<>();
            String type = properties.getProperty("db.type");
            String address = properties.getProperty("db.address");
            String name = properties.getProperty("db.name");
            String jdbcUrl = String.format("jdbc:%s:%s%s", type, address, "".equals(name) ? "" : "/".concat(name));
            result.put("jdbcUrl", jdbcUrl);
            result.put("driver", properties.getProperty("db.driver"));
            result.put("user", properties.getProperty("db.user"));
            result.put("password", properties.getProperty("db.password"));
            return result;
        }

        /**
         * Sets database parameters to the pool.
         *
         * @param pool Connection pool to configure.
         * @param data Map with needed data.
         */
        private void setParameters(BasicDataSource pool, Map<String, String> data) {
            pool.setUrl(data.get("jdbcUrl"));
            pool.setDriverClassName(data.get("driver"));
            pool.setUsername(data.get("user"));
            pool.setPassword(data.get("password"));
            pool.setMinIdle(5);
            pool.setMaxIdle(10);
            pool.setMaxOpenPreparedStatements(100);
        }

        /**
         * Returns Connection object to main Database class.
         *
         * @return Connection object.
         * @throws SQLException If Database problems occur.
         */
        private Connection getConnection() throws SQLException {
            return this.connectionPool.getConnection();
        }
    }

    /**
     * Holds sql queries for current database.
     *
     * @author Aleksei Sapozhnikov (vermucht@gmail.com)
     * @version 0.1
     * @since 0.1
     */
    private static class DatabaseQueries {
        /**
         * Map with sql queries.
         */
        private final Map<String, String> queries = new HashMap<>();

        /**
         * Constructs new instance using given properties.
         *
         * @param properties Object with queries.
         */
        private DatabaseQueries(Properties properties) {
            this.loadQueries(properties);
        }

        /**
         * Loads queries from files to memory.
         *
         * @param properties Properties object with file paths.
         */
        private void loadQueries(Properties properties) {
            this.loadStructuralQueries(properties);
            this.loadSeatQueries(properties);
            this.loadAccountQueries(properties);
            this.loadPaymentQueries(properties);
        }

        /**
         * Loads queries for structural changes in database: drop/create tables and functions,
         * clear all tables.
         *
         * @param properties Object with file paths.
         */
        private void loadStructuralQueries(Properties properties) {
            this.queries.put("sql.structure.tables.create",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.structure.tables.create")));
            this.queries.put("sql.structure.tables.drop",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.structure.tables.drop")));
            this.queries.put("sql.structure.functions.drop",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.structure.functions.drop")));
            this.queries.put("sql.structure.tables.clear",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.structure.tables.clear")));
        }

        /**
         * Loads queries to work with Seat objects.
         *
         * @param properties Object with file paths.
         */
        private void loadSeatQueries(Properties properties) {
            this.queries.put("sql.query.seat.add",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.query.seat.add")));
            this.queries.put("sql.query.seat.get_all",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.query.seat.get_all")));
            this.queries.put("sql.query.seat.get_by_place",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.query.seat.get_by_place")));
            this.queries.put("sql.query.seat.update_by_place",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.query.seat.update_by_place")));
        }

        /**
         * Loads queries to work with Account objects.
         *
         * @param properties Object with file paths.
         */
        private void loadAccountQueries(Properties properties) {
            this.queries.put("sql.query.account.add",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.query.account.add")));
            this.queries.put("sql.query.account.get_all",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.query.account.get_all")));
            this.queries.put("sql.query.account.get_by_name_phone",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.query.account.get_by_name_phone")));
        }

        /**
         * Loads queries to work with Payment objects.
         *
         * @param properties Object with file paths.
         */
        private void loadPaymentQueries(Properties properties) {
            this.queries.put("sql.query.payment.add",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.query.payment.add")));
            this.queries.put("sql.query.payment.get_all",
                    Utils.loadFileAsString(this, "UTF-8", properties.getProperty("sql.query.payment.get_all")));
        }

        /**
         * Returns query to main Database class.
         *
         * @param key Query key.
         * @return SQL query string.
         * @throws SqlQueryNotFoundException if query key was not found.
         */
        private String getQuery(String key) {
            if (!this.queries.containsKey(key)) {
                throw new SqlQueryNotFoundException(String.format("SQL query (key='%s') not found", key));
            }
            return this.queries.get(key);
        }
    }
}