package ru.job4j.music.dao.general;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.music.StaticMethods;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.String.format;
import static ru.job4j.music.dao.general.DbStructureChanger.Table.*;

/**
 * Class to make structural changes in database: create/drop tables, add/drop functions, etc.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class DbStructureChanger {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(DbStructureChanger.class);
    /**
     * Format of the property key for the "add" operation.
     */
    private static final String QUERY_FORMAT_CREATE_TABLE = "sql.table.%s";
    /**
     * Property key for "drop all tables" operation.
     */
    private static final String QUERY_DROP_ALL_TABLES_KEY = "sql.drop.tables";
    /**
     * Property key for "drop all functions" operation.
     */
    private static final String QUERY_DROP_ALL_FUNCTIONS_KEY = "sql.drop.functions";
    /**
     * File with connection pool properties.
     */
    private static final String DEFAULT_CONFIG = "ru/job4j/music/database.properties";
    /**
     * Database connection pool.
     */
    private final BasicDataSource pool;
    /**
     * Map with sql queries.
     */
    private final Map<Table, String> createTableQueries;
    /**
     * Sql query to drop all tables.
     */
    private final String queryDropAllTables;
    /**
     * Sql query to drop all functions.
     */
    private final String queryDropAllFunctions;

    /**
     * Constructs new object.
     *
     * @param config File with queries (as resource).
     * @param pool   Database pool object giving connections to db.
     */
    public DbStructureChanger(String config, BasicDataSource pool) {
        Properties prop = StaticMethods.loadProperties(config, this.getClass());
        this.pool = pool;
        this.createTableQueries = this.loadCreateTableQueries(prop);
        this.queryDropAllTables = prop.getProperty(QUERY_DROP_ALL_TABLES_KEY);
        this.queryDropAllFunctions = prop.getProperty(QUERY_DROP_ALL_FUNCTIONS_KEY);
    }

    /**
     * Constructs new object using default config file.
     *
     * @param pool Database pool object giving connections to db.
     */
    public DbStructureChanger(BasicDataSource pool) {
        this(DEFAULT_CONFIG, pool);
    }

    /**
     * Returns map with sql queries to create tables.
     *
     * @param prop Properties object with queries.
     * @return Map with queries to create tables.
     */
    private Map<Table, String> loadCreateTableQueries(Properties prop) {
        Map<Table, String> result = new HashMap<>();
        for (Table table : Table.values()) {
            result.put(
                    table,
                    prop.getProperty(format(QUERY_FORMAT_CREATE_TABLE, table.getTableName()))
            );
        }
        return result;
    }

    /**
     * Creates all needed tables.
     */
    public void createTables() {
        this.dbCreateTables(ROLES, ADDRESSES, MUSIC, USERS, USERS_MUSIC);
    }

    /**
     * Drops all tables existing in the database.
     */
    public void dropAllTables() {
        this.dbDropAllTables();
    }

    /**
     * Drops all functions existing in the database.
     */
    public void dropAllFunctions() {
        this.dbDropAllFunctions();
    }

    /**
     * Drops all tables and functions, then creates needed tables.
     */
    public void clear() {
        this.dropAllTables();
        this.dropAllFunctions();
        this.createTables();
    }

    /**
     * Performs dropping all tables in database.
     */
    private void dbDropAllTables() {
        try (Connection connection = this.pool.getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.execute(this.queryDropAllTables);
        } catch (SQLException e) {
            LOG.error(StaticMethods.describeException(e));
        }
    }

    /**
     * Performs dropping all functions in database.
     */
    private void dbDropAllFunctions() {
        try (Connection connection = this.pool.getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.execute(this.queryDropAllFunctions);
        } catch (SQLException e) {
            LOG.error(StaticMethods.describeException(e));
        }
    }

    /**
     * Performs creating needed tables in database.
     *
     * @param tables Tables to create - note that order matters if tables
     *               have references to each other.
     */
    private void dbCreateTables(Table... tables) {
        try (Connection connection = this.pool.getConnection();
             Statement statement = connection.createStatement()
        ) {
            for (Table table : tables) {
                String query = this.createTableQueries.get(table);
                statement.execute(query);
            }
        } catch (SQLException e) {
            LOG.error(StaticMethods.describeException(e));
        }
    }

    /**
     * Enumerates all tables needed.
     */
    public enum Table {
        ROLES("roles"),
        ADDRESSES("addresses"),
        MUSIC("music"),
        USERS("users"),
        USERS_MUSIC("users_music");

        /**
         * Name of the table for the entity.
         */
        private final String tableName;

        /**
         * Constructs new enum.
         *
         * @param tableName Name of the table for the entity.
         */
        Table(String tableName) {
            this.tableName = tableName;
        }

        /**
         * Returns table name.
         *
         * @return Table name.
         */
        public String getTableName() {
            return this.tableName;
        }
    }
}
