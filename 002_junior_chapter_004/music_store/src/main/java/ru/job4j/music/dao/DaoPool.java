package ru.job4j.music.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.music.StaticMethods;
import ru.job4j.music.dao.specific.RoleDao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.String.format;
import static ru.job4j.music.dao.DaoOperations.*;
import static ru.job4j.music.dao.DaoPool.DaoType.*;

/**
 * Class creating objects implementing DAO for different objects.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class DaoPool {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(DaoPool.class);
    /**
     * File with connection pool properties.
     */
    private static final String CONFIG_FILE = "ru/job4j/music/database.properties";
    /**
     * Format of the property key for the "add" operation.
     */
    private static final String QUERY_PROPERTY_FORMAT_ADD = "sql.query.%s.add";
    /**
     * Format of the property key for the "get by id" operation.
     */
    private static final String QUERY_PROPERTY_FORMAT_GET_BY_ID = "sql.query.%s.get_by_id";
    /**
     * Format of the property key for the "update" operation.
     */
    private static final String QUERY_PROPERTY_FORMAT_UPDATE = "sql.query.%s.update";
    /**
     * Format of the property key for the "delete" operation.
     */
    private static final String QUERY_PROPERTY_FORMAT_DELETE = "sql.query.%s.delete";
    /**
     * Format of the property key for the "get all" operation.
     */
    private static final String QUERY_PROPERTY_FORMAT_GET_ALL = "sql.query.%s.get_all";
    /**
     * Storage of all Dao in the pool.
     */
    private final Map<DaoType, Dao> daoStorage;

    /**
     * Constructs new object and creates all dao.
     *
     * @throws IOException If an I/O problem occurred.
     */
    public DaoPool(BasicDataSource connectionPool) throws IOException {
        Properties properties = StaticMethods.loadProperties(CONFIG_FILE, this.getClass());
        this.daoStorage = this.createAllDao(connectionPool, properties);
    }

    /**
     * Returns Dao of needed type.
     *
     * @param daoType Dao type needed.
     * @return Dao of needed type.
     */
    public Dao getDao(DaoType daoType) {
        return this.daoStorage.get(daoType);
    }

    /**
     * Creates all dao for the pool.
     *
     * @param connectionPool Connection pool which will be used by dao.
     * @param prop           Properties object containing queries.
     * @return Map with all created dao.
     */
    private Map<DaoType, Dao> createAllDao(BasicDataSource connectionPool, Properties prop) {
        Map<DaoType, Dao> result = new HashMap<>();
        result.put(ROLE, new RoleDao(connectionPool, this.loadQueries(prop, ROLE.toString())));
        return result;
    }

    /**
     * Returns map of queries for specific dao.
     *
     * @param prop   Properties where queries are stored.
     * @param daoKey String specifying for which class query is needed.
     *               The string is used to find needed property key.
     * @return Map with queries for specified dao.
     */
    private Map<DaoOperations, String> loadQueries(Properties prop, String daoKey) {
        Map<DaoOperations, String> result = new HashMap<>();
        String name = daoKey.toLowerCase();
        result.put(ADD, prop.getProperty(format(QUERY_PROPERTY_FORMAT_ADD, name)));
        result.put(GET_BY_ID, prop.getProperty(format(QUERY_PROPERTY_FORMAT_GET_BY_ID, name)));
        result.put(UPDATE, prop.getProperty(format(QUERY_PROPERTY_FORMAT_UPDATE, name)));
        result.put(DELETE, prop.getProperty(format(QUERY_PROPERTY_FORMAT_DELETE, name)));
        result.put(GET_ALL, prop.getProperty(format(QUERY_PROPERTY_FORMAT_GET_ALL, name)));
        return result;
    }

    /**
     * Enumerates all DAO in the Dao pool.
     *
     * @author Aleksei Sapozhnikov (vermucht@gmail.com)
     * @version 0.1
     * @since 0.1
     */
    public enum DaoType {
        USER("user"),
        ROLE("role"),
        ADDRESS("address"),
        MUSIC_GENRE("musicGenre");

        /**
         * Logger.
         */
        private static final Logger LOG = LogManager.getLogger(DaoType.class);
        /**
         * Field with string representation of the enum.
         */
        private final String string;

        /**
         * Constructor.
         *
         * @param value String representation of the enum.
         */
        DaoType(String value) {
            this.string = value;
        }

        /**
         * Returns string representation.
         *
         * @return String representation of the enum.
         */
        @Override
        public String toString() {
            return this.string;
        }
    }
}
