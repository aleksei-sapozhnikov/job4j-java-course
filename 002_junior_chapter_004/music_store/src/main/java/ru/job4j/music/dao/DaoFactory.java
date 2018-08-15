package ru.job4j.music.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.String.format;
import static ru.job4j.music.dao.DaoFactory.ConnectionPoolProperties.*;
import static ru.job4j.music.dao.DaoFactory.DaoPerformers.USER;
import static ru.job4j.music.dao.DaoOperations.ADD;
import static ru.job4j.music.dao.DaoOperations.GET_BY_ID;

/**
 * Class creating objects implementing DAO for different objects.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class DaoFactory {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(DaoFactory.class);
    /**
     * Properties file loaded as resource.
     */
    private static final String PROPERTIES_PATH = "ru/job4j/music/database.properties";

    private final Map<DaoPerformers, DaoPerformer> daoPerformers;

    public DaoFactory() throws IOException {
        Properties properties = this.loadProperties(PROPERTIES_PATH);
        BasicDataSource connectionPool = new BasicDataSource();
        this.configureConnectionPool(connectionPool, properties);
        this.daoPerformers = this.createDaoPerformers(connectionPool, properties);
    }

    public DaoPerformer getDaoPerformer(DaoPerformers daoPerformers) {
        return this.daoPerformers.get(daoPerformers);
    }

    private Map<DaoPerformers, DaoPerformer> createDaoPerformers(BasicDataSource connectionPool, Properties prop) {
        Map<DaoPerformers, DaoPerformer> result = new HashMap<>();
        result.put(USER, new DaoPerformerUser(connectionPool, this.loadDaoQueries(prop, USER.string().toLowerCase())));
        return result;
    }

    private Map<DaoOperations, String> loadDaoQueries(Properties prop, String className) {
        Map<DaoOperations, String> result = new HashMap<>();
        result.put(ADD, prop.getProperty(format("sql.%s.add", className)));
        result.put(GET_BY_ID, prop.getProperty(format("sql.%s.get_by_id", className)));
        return result;
    }

    /**
     * Loads properties file using ClassLoader.
     *
     * @param propFile path to the properties file
     *                 e.g: "ru/job4j/vacancies/main.properties"
     * @return Properties object with values read from file.
     * @throws IOException If problems happened with reading from/to InputStream.
     */
    private Properties loadProperties(String propFile) throws IOException {
        Properties props = new Properties();
        ClassLoader loader = this.getClass().getClassLoader();
        try (InputStream input = loader.getResourceAsStream(propFile)) {
            props.load(input);
        }
        return props;
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
        String url = format("jdbc:%s:%s%s", values.get(DB_TYPE), values.get(DB_ADDRESS),
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
     * Enum describing all properties needed by collection pool to connect to database.
     */
    public enum ConnectionPoolProperties {
        DB_TYPE,
        DB_DRIVER,
        DB_ADDRESS,
        DB_NAME,
        DB_USER,
        DB_PASSWORD
    }

    /**
     * Enumerates all DAO performers produced by this DaoFactory.
     *
     * @author Aleksei Sapozhnikov (vermucht@gmail.com)
     * @version 0.1
     * @since 0.1
     */
    public enum DaoPerformers {
        USER("user"),
        ROLE("role"),
        ADDRESS("address"),
        MUSIC_GENRE("musicGenre");

        /**
         * Logger.
         */
        private static final Logger LOG = LogManager.getLogger(DaoPerformers.class);

        private final String value;

        DaoPerformers(String value) {
            this.value = value;
        }

        public String string() {
            return this.value;
        }
    }
}
