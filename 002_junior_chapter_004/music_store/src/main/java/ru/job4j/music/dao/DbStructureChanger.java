package ru.job4j.music.dao;

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
import static ru.job4j.music.dao.DbStructureChanger.Table.*;

/**
 * TODO: class description
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
    private static final String FORMAT_CREATE_TABLE = "sql.table.%s";
    /**
     * File with connection pool properties.
     */
    private static final String CONFIG_FILE = "ru/job4j/music/database.properties";
    /**
     * Database connector.
     */
    private final DbConnector connector;
    /**
     * Map with sql queries.
     */
    private final Map<Table, String> queries;

    public DbStructureChanger(DbConnector connector) {
        Properties prop = StaticMethods.loadProperties(CONFIG_FILE, this.getClass());
        this.connector = connector;
        this.queries = this.loadQueries(prop);
    }

    private Map<Table, String> loadQueries(Properties prop) {
        Map<Table, String> result = new HashMap<>();
        for (Table table : Table.values()) {
            result.put(
                    table,
                    prop.getProperty(format(FORMAT_CREATE_TABLE, table.name()))
            );
        }
        return result;
    }

    public void createTables() {
        this.createTables(ROLE, ADDRESS, MUSIC, USER, USER_MUSIC);
    }

    public void dropAllTables() {

    }

    private void createTables(Table... tables) {
        try (Connection connection = this.connector.getConnection();
             Statement statement = connection.createStatement()
        ) {
            for (Table table : tables) {
                String query = this.queries.get(table);
                statement.execute(query);
            }
        } catch (SQLException e) {
            LOG.error(StaticMethods.describeException(e));
        }
    }

    public enum Table {
        ROLE("roles"),
        ADDRESS("addresses"),
        MUSIC("music"),
        USER("users"),
        USER_MUSIC("users_music");

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
