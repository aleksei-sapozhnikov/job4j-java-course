package ru.job4j.music.dao.general;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.music.StaticMethods;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.String.format;
import static ru.job4j.music.dao.general.DbConnector.ConnectionPoolProperties.*;

/**
 * Class to connect to database and give connction pool to others.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class DbConnector {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(DbConnector.class);
    /**
     * File with connection pool properties.
     */
    private static final String DEFAULT_CONFIG = "ru/job4j/music/database.properties";
    /**
     * Connection pool to database.
     */
    private final BasicDataSource pool;

    /**
     * Constructs new object.
     *
     * @param config Way to resource file with connection properties.
     */
    public DbConnector(String config) {
        Properties properties = StaticMethods.loadProperties(config, this.getClass());
        this.pool = new BasicDataSource();
        this.configureConnectionPool(this.pool, properties);
    }

    /**
     * Constructs new object using default config.
     */
    public DbConnector() {
        this(DEFAULT_CONFIG);
    }

    /**
     * Returns connection pool.
     *
     * @return Connection pool for use.
     */
    public BasicDataSource getPool() throws SQLException {
        return this.pool;
    }


    /**
     * Configures database connection pool.
     *
     * @param pool Pool to configure.
     * @param prop Map of connection properties.
     */
    private void configureConnectionPool(BasicDataSource pool, Properties prop) {
        Map<ConnectionPoolProperties, String> values = this.loadConnectionConfiguration(prop);
        String dbName = values.get(DB_NAME);
        String url = format(
                "jdbc:%s:%s%s",
                values.get(DB_TYPE),
                values.get(DB_ADDRESS),
                "".equals(dbName) ? "" : "/".concat(dbName)
        );
        pool.setDriverClassName(values.get(DB_DRIVER));
        pool.setUrl(url);
        pool.setUsername(values.get(DB_USER));
        pool.setPassword(values.get(DB_PASSWORD));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }


    /**
     * Reads connection pool properties and returns map of needed values.
     *
     * @param prop Property object where from to read values.
     * @return Map of needed connection pool properties.
     */
    private Map<ConnectionPoolProperties, String> loadConnectionConfiguration(Properties prop) {
        Map<ConnectionPoolProperties, String> result = new HashMap<>();
        result.put(DB_TYPE, prop.getProperty("db.type"));
        result.put(DB_DRIVER, prop.getProperty("db.driver"));
        result.put(DB_ADDRESS, prop.getProperty("db.address"));
        result.put(DB_NAME, prop.getProperty("db.name"));
        result.put(DB_USER, prop.getProperty("db.user"));
        result.put(DB_PASSWORD, prop.getProperty("db.password"));
        return result;
    }

    /**
     * Enumerates properties needed by collection pool to connect to database.
     */
    public enum ConnectionPoolProperties {
        DB_TYPE,
        DB_DRIVER,
        DB_ADDRESS,
        DB_NAME,
        DB_USER,
        DB_PASSWORD
    }
}
